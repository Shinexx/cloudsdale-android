package org.cloudsdale.android.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import org.cloudsdale.android.PersistentData;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.api_models.Cloud;

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
		if (getArguments().containsKey(CloudDetailFragment.ARG_ITEM_ID)) {
			this.cloud = PersistentData.getCloud(getActivity(), getArguments()
					.getString(CloudDetailFragment.ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Get the view
		View rootView = inflater.inflate(R.layout.fragment_cloud_detail,
				container, false);

		if (this.cloud != null) {
			// Set the fragment title to name
			getActivity().setTitle(this.cloud.getName());

			// Set the content to the cloud's name (testing storage)
			((TextView) rootView.findViewById(R.id.cloud_detail))
					.setText(this.cloud.getName());
		} else {
			Log.d(CloudDetailFragment.TAG, "Cloud is null");
		}
		return rootView;
	}
}
