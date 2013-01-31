package org.cloudsdale.android.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;

import org.cloudsdale.android.R;
import org.cloudsdale.android.ui.StaticNavigation;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.ListFragment;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;

public class NavigationFragment extends ListFragment {

	AQuery	aq;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		aq = new AQuery(getActivity());
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ListView view = (ListView) getListView();

		// Header
		TextView header = new TextView(getActivity());
		header.setText(R.string.fragment_navigation_header);
		view.addHeaderView(header);

		// Body content
		for (StaticNavigation nav : StaticNavigation.values()) {
			View navView = inflater.inflate(R.layout.widget_static_navigation,
					null);
			aq.id(navView).id(R.id.static_navigation_icon)
					.image(nav.getResId());
			aq.id(navView).id(R.id.static_navigation_label)
					.text(nav.getDisplayName());
			view.addView(navView);
		}

		return view;
	}

}
