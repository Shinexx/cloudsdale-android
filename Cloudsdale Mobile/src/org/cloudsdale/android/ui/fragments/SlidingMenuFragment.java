package org.cloudsdale.android.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.commonsware.cwac.merge.MergeAdapter;

import org.cloudsdale.android.R;
import org.cloudsdale.android.ui.adapters.StaticNavigationAdapter;

public class SlidingMenuFragment extends ListFragment {

	public static MergeAdapter generateAdapter(Context context) {
		LayoutInflater inflator = LayoutInflater.from(context);
		MergeAdapter adapter = new MergeAdapter();

		View navHeader = inflator.inflate(
				R.layout.widget_static_navigation_header, null);
		adapter.addView(navHeader);
		adapter.addAdapter(new StaticNavigationAdapter(context));

		return adapter;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setListAdapter(generateAdapter(getActivity()));
		return inflater.inflate(R.layout.widget_sliding_menu, container, false);
	}

}
