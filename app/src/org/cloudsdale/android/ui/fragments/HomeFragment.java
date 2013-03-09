package org.cloudsdale.android.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.api.User;
import org.cloudsdale.android.ui.widget.NowLayout;

/**
 * Fragment that displays a home view with all the user accounts logged into the
 * device.<br/>
 * Copyright (c) 2013 Cloudsdale.org
 * 
 * @author Jamison Greeley (berwyn.codeweaver@gmail.com)
 * 
 */
public class HomeFragment extends Fragment {

	private static final String	TAG	= "Home Fragment";

	private Cloudsdale			mAppInstance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mAppInstance = (Cloudsdale) getActivity().getApplication();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		AQuery aq = new AQuery(getActivity());
		View host = aq.inflate(null, R.layout.fragment_home, container);

		return host;
	}
	
	private void clearProgressViews() {
		AQuery aq = new AQuery(getActivity());
		aq.id(R.id.home_progress_bar).gone();
		aq.id(R.id.home_progress_text).gone();
	}

	public void inflateHomeCards(User... users) {
		AQuery aq = new AQuery(getActivity());
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		clearProgressViews();
		aq.id(R.id.home_card_host).visible();
		for (User u : users) {
			if (mAppInstance.isDebuggable()) {
				Log.d(TAG, String.format("Inflating user %1s", u.getName()));
			}
			View card = inflater.inflate(R.layout.widget_home_account_card,
					null);
			((NowLayout) getView().findViewById(R.id.home_card_host))
					.addView(card);
			String joinDate = DateFormat.getLongDateFormat(getActivity())
					.format(u.getMemberSince());
			aq.find(R.id.home_card_username).text(u.getName());
			aq.id(R.id.home_card_join_date).text(
					R.string.fragment_home_join_date_text, joinDate);
			aq.id(R.id.home_card_cloud_count).text(
					R.string.fragment_home_cloud_count_text,
					u.getClouds().size());
			aq.id(R.id.home_card_quickbadge).image(u.getAvatar().getNormal());
		}
	}
}
