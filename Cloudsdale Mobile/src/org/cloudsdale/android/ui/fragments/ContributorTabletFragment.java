package org.cloudsdale.android.ui.fragments;

import org.cloudsdale.android.R;
import org.cloudsdale.android.ui.adapters.ContributorGridAdapter;
import org.cloudsdale.android.ui.widget.ExpandableHeightGridView;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContributorTabletFragment extends Fragment {

	private ExpandableHeightGridView	mGridView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		mGridView = (ExpandableHeightGridView) inflater.inflate(
		        R.layout.fragment_contributor_tablet, null);
		mGridView.setExpanded(true);

		addContributors(inflater);

		return mGridView;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void addContributors(LayoutInflater inflater) {
		mGridView.setAdapter(new ContributorGridAdapter(getActivity()));
	}

}
