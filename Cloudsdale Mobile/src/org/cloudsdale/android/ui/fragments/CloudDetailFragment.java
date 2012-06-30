package org.cloudsdale.android.ui.fragments;

import org.cloudsdale.android.PersistentData;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.api_models.Cloud;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class CloudDetailFragment extends SherlockFragment {

	public static final String	TAG			= "CloudDetailFragment";
	public static final String	ARG_ITEM_ID	= "item_id";

	Cloud						cloud;
	String						debugStuff;

	public CloudDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			cloud = PersistentData.getCloud(getActivity(), getArguments()
					.getString(ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Get the view
		View rootView = inflater.inflate(R.layout.fragment_cloud_detail,
				container, false);

		if (cloud != null) {
			// Set the fragment title to name
			getActivity().setTitle(cloud.getName());

			// Set the content to the cloud's name (testing storage)
			((TextView) rootView.findViewById(R.id.cloud_detail)).setText(cloud
					.getName());
		} else {
			Log.d(TAG, "Cloud is null");
		}
		return rootView;
	}
}
