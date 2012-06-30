package org.cloudsdale.android.ui.fragments;

import java.util.ArrayList;

import org.cloudsdale.android.PersistentData;
import org.cloudsdale.android.models.api_models.Cloud;
import org.cloudsdale.android.ui.adapters.CloudsAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

public class CloudListFragment extends SherlockListFragment {

	private static final String	STATE_ACTIVATED_POSITION	= "activated_position";

	private Callbacks			mCallbacks					= sDummyCallbacks;
	private int					mActivatedPosition			= ListView.INVALID_POSITION;

	public interface Callbacks {

		public void onItemSelected(String id);
	}

	private static Callbacks	sDummyCallbacks	= new Callbacks() {
													@Override
													public void onItemSelected(
															String id) {
													}
												};

	public CloudListFragment() {
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
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) { throw new IllegalStateException(
				"Activity must implement fragment's callbacks."); }

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);
		// Get the cloud
		Cloud selectedCloud = (Cloud) listView.getAdapter().getItem(position);
		
		// Send the cloud id
		mCallbacks.onItemSelected(selectedCloud.getId());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	public void setActivateOnItemClick(boolean activateOnItemClick) {
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	public void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
}
