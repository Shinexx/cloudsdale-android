package org.cloudsdale.android.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.commonsware.cwac.merge.MergeAdapter;

import org.cloudsdale.android.R;
import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.ui.StaticNavigation;
import org.cloudsdale.android.ui.adapters.CloudAdapter;
import org.cloudsdale.android.ui.adapters.StaticNavigationAdapter;

import lombok.Setter;

public class SlidingMenuFragment extends ListFragment {

	@Setter
	private ISlidingMenuFragmentCallbacks	callback;

	public static interface ISlidingMenuFragmentCallbacks {
		public void listItemClicked(String id, Class<?> clazz);
	}

	public MergeAdapter generateAdapter(Context context) {
		LayoutInflater inflator = LayoutInflater.from(context);
		MergeAdapter adapter = new MergeAdapter();

		View navHeader = inflator.inflate(
				R.layout.widget_static_navigation_header, null);
		adapter.addView(navHeader);
		adapter.addAdapter(new StaticNavigationAdapter(context));
		View cloudHeader = inflator.inflate(R.layout.widget_cloud_header, null);
		adapter.addView(cloudHeader);
		CloudAdapter cloudAdapter = new CloudAdapter(null, getActivity());
		adapter.addAdapter(cloudAdapter);

		return adapter;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// create ContextThemeWrapper from the original Activity Context with
		// the custom theme
		Context context = new ContextThemeWrapper(getActivity(),
				R.style.Theme_CloudsdaleDark);
		setListAdapter(generateAdapter(context));
		// clone the inflater using the ContextThemeWrapper
		LayoutInflater localInflater = inflater.cloneInContext(context);
		// inflate using the cloned inflater, not the passed in default
		return localInflater.inflate(R.layout.widget_sliding_menu, container,
				false);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		MergeAdapter adapter = (MergeAdapter) getListAdapter();
		if (adapter.getItem(position) instanceof StaticNavigation) {
			callback.listItemClicked(
					((StaticNavigation) adapter.getItem(position)).getTextId(),
					StaticNavigation.class);
		} else if (adapter.getItem(position) instanceof Cloud) {
			callback.listItemClicked(
					((Cloud) adapter.getItem(position)).getId(), Cloud.class);
		}
	}

}