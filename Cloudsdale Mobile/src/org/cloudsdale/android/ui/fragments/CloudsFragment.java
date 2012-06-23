package org.cloudsdale.android.ui.fragments;

import java.util.ArrayList;
import java.util.Arrays;

import org.cloudsdale.android.PersistentData;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.Cloud;
import org.cloudsdale.android.queries.UserCloudQuery;
import org.cloudsdale.android.ui.CloudListAdapter;
import org.cloudsdale.android.ui.MainViewActivity.ChatActivity;
import org.cloudsdale.android.ui.MainViewActivity.ChatFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class CloudsFragment extends SherlockFragment {

	boolean					mDualPane;
	int						mSelectedCloud	= 0;

	PullToRefreshListView	ptr;
	CloudListAdapter		mAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Check to see if we're in dual pane mode
		View chatFrame = getActivity().findViewById(R.id.chat_view);
		mDualPane = chatFrame != null
				&& chatFrame.getVisibility() == View.VISIBLE;

		// Get the cloud view
		View cloudView = getActivity().findViewById(R.id.clouds_view);

		// Store the ptr view
		ptr = (PullToRefreshListView) cloudView.findViewById(R.id.cloud_ptr);
		ptr.setOnRefreshListener(new RefreshListener());
		ptr.setDisableScrollingWhileRefreshing(true);
		addPtrClouds();

		// Restore saved state if it exists
		if (savedInstanceState != null) {
			mSelectedCloud = savedInstanceState.getInt("selectedCloud", 0);
		}

		// Make selection visible is dual pane
		if (mDualPane) {
			showChat(mSelectedCloud);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.cloud_list_view, container, false);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save the selected cloud
		outState.putInt("selectedCloud", mSelectedCloud);
	}

	/**
	 * Helper function to show a chat when it's clicked
	 * 
	 * @param index
	 *            index of the cloud that's been selected
	 */
	private void showChat(int index) {
		mSelectedCloud = index;

		if (mDualPane) {
			// Check the fragment, replace as needed
			ChatFragment chatFrame = (ChatFragment) getFragmentManager()
					.findFragmentById(R.id.chat_view);
			if (chatFrame == null || chatFrame.getShownCloud() != index) {
				// Make the new fragment to show
				chatFrame = ChatFragment.newInstance(index);

				// Execute the transaction, replace whatever fragment is
				// showing
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.chat_view, chatFrame);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.commit();
			} else {
				// Launch the activity
				Intent intent = new Intent();
				intent.setClass(getSherlockActivity(), ChatActivity.class);
				intent.putExtra("index", index);
			}
		}
	}

	private void addPtrClouds() {
		ArrayList<Cloud> clouds = PersistentData.getMe(getActivity())
				.getClouds();
		mAdapter = new CloudListAdapter(getActivity(), clouds);

		ptr.getRefreshableView().setAdapter(mAdapter);
	}

	public class RefreshListener implements PullToRefreshBase.OnRefreshListener {

		@Override
		public void onRefresh() {
			String[] params = { getString(R.string.cloudsdale_api_url)
					+ "/users/" + PersistentData.getMe(getActivity()).getId()
					+ "/clouds" };
			new InnerCloudQuery().execute(params);
		}

	}

	private class InnerCloudQuery extends UserCloudQuery {

		@Override
		protected void onPostExecute(String result) {

			Gson gson = new Gson();
			Cloud[] clouds = gson.fromJson(result, Cloud[].class);
			ArrayList<Cloud> cloudList = new ArrayList<Cloud>(
					Arrays.asList(clouds));
			ptr.removeAllViews();
			mAdapter = new CloudListAdapter(CloudsFragment.this.getActivity(),
					cloudList);
			ptr.getRefreshableView().setAdapter(mAdapter);
			ptr.onRefreshComplete();
			super.onPostExecute(result);
		}

	}

}
