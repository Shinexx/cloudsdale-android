package org.cloudsdale.android.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

import org.cloudsdale.android.PersistentData;
import org.cloudsdale.android.models.api_models.Cloud;
import org.cloudsdale.android.ui.CloudDetailActivity;
import org.cloudsdale.android.ui.adapters.CloudsAdapter;

import java.util.ArrayList;

public class CloudListFragment extends SherlockListFragment {

	public interface Callbacks {

		public void onItemSelected(String id);
	}

	private static final String	STATE_ACTIVATED_POSITION	= "activated_position";
	private Callbacks			mCallbacks					= CloudListFragment.sDummyCallbacks;

	private int					mActivatedPosition			= ListView.INVALID_POSITION;

	private static Callbacks	sDummyCallbacks				= new Callbacks() {
																@Override
																public void onItemSelected(
																		String id) {
																}
															};

	public CloudListFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
//		if (!(activity instanceof Callbacks)) { throw new IllegalStateException(
//				"Activity must implement fragment's callbacks."); }
//
//		this.mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get the user's clouds
		ArrayList<Cloud> myClouds = PersistentData.getMe(getActivity())
				.getClouds();

		// Build the adapter
		setListAdapter(new CloudsAdapter(getSherlockActivity(), myClouds));
	}

	@Override
	public void onDetach() {
		super.onDetach();
		this.mCallbacks = CloudListFragment.sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);
		// Get the cloud
		Cloud selectedCloud = (Cloud) listView.getAdapter().getItem(position);

		// Send the cloud id
//		this.mCallbacks.onItemSelected(selectedCloud.getId());
		Intent detailIntent = new Intent(getSherlockActivity(), CloudDetailActivity.class);
		detailIntent.putExtra(CloudDetailFragment.ARG_ITEM_ID, id);
		startActivity(detailIntent);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (this.mActivatedPosition != AdapterView.INVALID_POSITION) {
			outState.putInt(CloudListFragment.STATE_ACTIVATED_POSITION,
					this.mActivatedPosition);
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (savedInstanceState != null
				&& savedInstanceState
						.containsKey(CloudListFragment.STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(CloudListFragment.STATE_ACTIVATED_POSITION));
		}
	}

	public void setActivatedPosition(int position) {
		if (position == AdapterView.INVALID_POSITION) {
			getListView().setItemChecked(this.mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		this.mActivatedPosition = position;
	}

	public void setActivateOnItemClick(boolean activateOnItemClick) {
		getListView().setChoiceMode(
				activateOnItemClick ? AbsListView.CHOICE_MODE_SINGLE
						: AbsListView.CHOICE_MODE_NONE);
	}
}
