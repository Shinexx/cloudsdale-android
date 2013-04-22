package org.cloudsdale.android.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.androidquery.AQuery;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

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
@EFragment(R.layout.fragment_home)
public class HomeFragment extends Fragment {

	private static final String	TAG	= "Home Fragment";

	@App
	Cloudsdale					cloudsdale;

	@ViewById(R.id.home_login_content)
	View						loginProgress;
	@ViewById(R.id.home_content)
	View						profileCards;

	private AQuery				aQuery;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		aQuery = new AQuery(getActivity());
		super.onCreate(savedInstanceState);
	}
	
	public void inflateHomeCards(User... users) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		aQuery.id(loginProgress).gone();
		aQuery.id(profileCards).visible();
		for (User u : users) {
			if (cloudsdale.isDebuggable()) {
				Log.d(TAG, String.format("Inflating user %1s", u.getName()));
			}
			View card = inflater.inflate(R.layout.widget_home_account_card,
					null);
			((NowLayout) getView().findViewById(R.id.home_card_host))
					.addView(card);
			aQuery = aQuery.id(card);
			String joinDate = DateFormat.getLongDateFormat(getActivity())
					.format(u.getMemberSince());
			aQuery.find(R.id.home_card_username).text(u.getName());
			aQuery.id(R.id.home_card_join_date).text(
					R.string.fragment_home_join_date_text, joinDate);
			aQuery.id(R.id.home_card_cloud_count).text(
					R.string.fragment_home_cloud_count_text,
					u.getClouds().size());
			aQuery.id(R.id.home_card_quickbadge).image(
					u.getAvatar().getNormal());
		}
	}
}
