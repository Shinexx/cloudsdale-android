package org.cloudsdale.android.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.commonsware.cwac.merge.MergeAdapter;

import org.cloudsdale.android.R;

public class SlidingMenuAdapter extends MergeAdapter {

	public SlidingMenuAdapter(Activity context) {
		super();

		LayoutInflater inflator = LayoutInflater.from(context);

		View navHeader = inflator.inflate(
				R.layout.widget_static_navigation_header, null);
		addView(navHeader);
		addAdapter(new StaticNavigationAdapter(context));
		View cloudHeader = inflator.inflate(R.layout.widget_cloud_header, null);
		addView(cloudHeader);
		CloudAdapter cloudAdapter = new CloudAdapter(null, context);
		addAdapter(cloudAdapter);
	}
	
	public StaticNavigationAdapter getStaticNavigationAdapter() {
		return (StaticNavigationAdapter) getPieces().get(1);
	}
	
	public CloudAdapter getCloudAdapter() {
		return (CloudAdapter) getPieces().get(3);
	}

}
