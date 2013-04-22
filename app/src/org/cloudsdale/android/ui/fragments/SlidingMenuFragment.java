package org.cloudsdale.android.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.commonsware.cwac.merge.MergeAdapter;

import org.cloudsdale.android.R;
import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.ui.StaticNavigation;
import org.cloudsdale.android.ui.adapters.SlidingMenuAdapter;

import lombok.Setter;

public class SlidingMenuFragment extends ListFragment {

	@Setter
	private ISlidingMenuFragmentCallbacks	callback;

	public static interface ISlidingMenuFragmentCallbacks {
		public void listItemClicked(String id, Class<?> clazz);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setListAdapter(new SlidingMenuAdapter(getActivity()));
		return inflater.inflate(R.layout.widget_sliding_menu, container, false);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (callback != null) {
			MergeAdapter adapter = (MergeAdapter) getListAdapter();
			if (adapter.getItem(position) instanceof StaticNavigation) {
				callback.listItemClicked(((StaticNavigation) adapter
						.getItem(position)).getTextId(), StaticNavigation.class);
			} else if (adapter.getItem(position) instanceof Cloud) {
				callback.listItemClicked(
						((Cloud) adapter.getItem(position)).getId(),
						Cloud.class);
			}
		}
	}

}