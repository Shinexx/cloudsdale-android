package org.cloudsdale.android.ui.fragments;

import java.util.ArrayList;

import org.cloudsdale.android.PersistentData;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.Cloud;
import org.cloudsdale.android.ui.CloudListAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

public class CloudListFragment extends SherlockFragment {

	ListView	mList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Get the view
		View cloudListView = inflater.inflate(R.layout.cloud_list_view,
				container, false);

		// Get the list in the view
		mList = (ListView) cloudListView.findViewById(R.id.cloud_list);

		// Create the adapter
		ArrayList<Cloud> clouds = PersistentData.getMe(getActivity())
				.getClouds();
		CloudListAdapter listAdapter = new CloudListAdapter(getActivity(),
				clouds);

		// Set the adapter
		mList.setAdapter(listAdapter);

		return cloudListView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		// Get the user's clouds
	}

}
