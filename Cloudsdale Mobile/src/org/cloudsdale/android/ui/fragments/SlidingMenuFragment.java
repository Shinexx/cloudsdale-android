package org.cloudsdale.android.ui.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.StaticNavigation;
import org.cloudsdale.android.managers.UserManager;
import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.api.User;
import org.cloudsdale.android.ui.AboutActivity;
import org.cloudsdale.android.ui.CloudActivity;
import org.cloudsdale.android.ui.HomeActivity;

/**
 * View fragment to show the inner view of the sliding menu.
 * Copyright (c) 2012 Cloudsale.org
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class SlidingMenuFragment extends SherlockFragment {

	private ScrollView		mRootView;
	private LinearLayout	mStaticNavRoot;
	private LinearLayout	mCloudNavRoot;
	private View			mHeaderLoadingView;
	private View			mHeaderContentView;
	private ImageView		mHeaderAvatar;
	private TextView		mHeaderUsername;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = (ScrollView) inflater.inflate(R.layout.fragment_sliding_menu, null);
		View headerView = mRootView.findViewById(R.id.sliding_menu_header);

		// Get the header views
		mHeaderLoadingView = headerView
				.findViewById(R.id.sliding_menu_header_loading_view);
		mHeaderContentView = headerView
				.findViewById(R.id.sliding_menu_header_content_view);
		mHeaderAvatar = (ImageView) headerView
				.findViewById(R.id.sliding_menu_header_avatar);
		mHeaderUsername = (TextView) headerView
				.findViewById(R.id.sliding_menu_header_username_label);

		// Get the static nav views
		mStaticNavRoot = (LinearLayout) mRootView
				.findViewById(R.id.sliding_menu_static_nav_root);

		// Get the cloud nav views
		mCloudNavRoot = (LinearLayout) mRootView
				.findViewById(R.id.sliding_menu_cloud_nav_root);

		// Fill the nav
		setStaticNavigation();

		// Start the task
		new ViewFillTask().execute();

		return mRootView;
	}

	/**
	 * Navigate the view hierarchy from the menu
	 * 
	 * @param viewId
	 */
	private void navigate(String viewId) {
		Intent intent = new Intent();
		if (viewId.equals("Home")) {
			intent.setClass(getActivity(), HomeActivity.class);
			// Clear all tasks when returning home
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		} else if (viewId.equals("Settings")) {
			// TODO Start the settings view
			intent.setClass(getActivity(), HomeActivity.class);
		} else if (viewId.equals("Logout")) {
			// TODO Logout the user
			intent.setClass(getActivity(), HomeActivity.class);
		} else if (viewId.equals("Explore")) {
			// TODO Start the explore view
			intent.setClass(getActivity(), HomeActivity.class);
		} else if (viewId.equals("About")) {
			intent.setClass(getActivity(), AboutActivity.class);
		} else {
			intent.setClass(getActivity(), CloudActivity.class);
			intent.putExtra("cloudId", viewId);
		}
		startActivity(intent);
	}

	/**
	 * Fills the header view with the appropriate user data
	 * 
	 * @param me
	 *            The logged in user
	 */
	private void setSlideMenuHeader(User me) {
		UrlImageViewHelper.setUrlDrawable(mHeaderAvatar, me.getAvatar()
				.getNormal(), R.drawable.ic_unknown_user,
				Cloudsdale.AVATAR_EXPIRATION);
		mHeaderUsername.setText(me.getName());
		mHeaderLoadingView.setVisibility(View.GONE);
		mHeaderContentView.setVisibility(View.VISIBLE);
	}

	private void setSlideMenuClouds(User me) {
		for (Cloud c : me.getClouds()) {
			LayoutInflater inflater = getLayoutInflater(getArguments());
			View cloudView = inflater
					.inflate(R.layout.fragment_sliding_menu_entry, null);
			ImageView icon = (ImageView) cloudView
					.findViewById(R.id.cloud_icon);
			TextView text = (TextView) cloudView.findViewById(R.id.cloud_name);
			TextView hiddenId = (TextView) cloudView
					.findViewById(R.id.cloud_hidden_id);

			UrlImageViewHelper.setUrlDrawable(icon, c.getAvatar().getNormal(),
					R.drawable.ic_unknown_cloud, Cloudsdale.CLOUD_EXPIRATION);
			text.setText(c.getName());
			hiddenId.setText(c.getId());
			cloudView.setId(c.getId().hashCode());
			cloudView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					navigate(((TextView) v.findViewById(R.id.cloud_hidden_id))
							.getText().toString());
				}
			});

			mCloudNavRoot.addView(cloudView);
		}
	}

	/**
	 * Fills the static navigation views from the StaticNavigation enum
	 */
	private void setStaticNavigation() {
		for (StaticNavigation nav : StaticNavigation.values()) {
			LayoutInflater inflater = getLayoutInflater(getArguments());
			View navView = inflater.inflate(R.layout.fragment_sliding_menu_entry, null);
			ImageView icon = (ImageView) navView.findViewById(R.id.cloud_icon);
			TextView text = (TextView) navView.findViewById(R.id.cloud_name);
			TextView hiddenId = (TextView) navView
					.findViewById(R.id.cloud_hidden_id);

			icon.setImageDrawable(getResources().getDrawable(nav.getResId()));
			text.setText(nav.getDisplayName());
			hiddenId.setText(nav.getTextId());
			navView.setId(nav.getTextId().hashCode());
			navView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					navigate(((TextView) v.findViewById(R.id.cloud_hidden_id))
							.getText().toString());
				}
			});

			mStaticNavRoot.addView(navView);
		}
	}

	/**
	 * An AsyncTask to fetch the logged in user and fill the UI views
	 * asynchronously.
	 * Copyright (c) 2012 Cloudsdale.org
	 * 
	 * @author Jamison Greeley (atomicrat2552@gmail.com)
	 * 
	 */
	class ViewFillTask extends AsyncTask<Void, Void, User> {

		@Override
		protected User doInBackground(Void... arg0) {
			return UserManager.getLoggedInUser();
		}

		@Override
		protected void onPostExecute(User result) {
			setSlideMenuHeader(result);
			setSlideMenuClouds(result);
			super.onPostExecute(result);
		}

	}

}