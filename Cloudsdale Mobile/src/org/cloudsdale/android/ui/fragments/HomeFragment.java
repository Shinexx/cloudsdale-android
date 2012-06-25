package org.cloudsdale.android.ui.fragments;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.cloudsdale.android.PersistentData;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.Role;
import org.cloudsdale.android.models.User;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class HomeFragment extends SherlockFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view
		View homeView = inflater.inflate(R.layout.home_view, container, false);

		// Get the user
		User me = PersistentData.getMe(getActivity());

		// Get the view elements
		ImageView userIcon = (ImageView) homeView
				.findViewById(R.id.user_home_icon);
		TextView userName = (TextView) homeView
				.findViewById(R.id.user_home_display_name);
		TextView registeredText = (TextView) homeView
				.findViewById(R.id.user_home_registered_text);
		TextView cloudCount = (TextView) homeView
				.findViewById(R.id.user_home_cloud_count);
		TextView siteRole = (TextView) homeView
				.findViewById(R.id.user_home_site_role);
		TextView warningCount = (TextView) homeView
				.findViewById(R.id.user_home_warnings_count);

		// Setup the proper date string
		Calendar registered = me.getMemberSince();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		String registeredDate = " " + sdf.format(registered.getTime());

		// Setup the proper role string
		Role userRole = me.getRole();
		String roleString = "";
		switch (userRole) {
			case NORMAL:
			case DONOR:
			case MODERATOR:
			case PLACEHOLDER:
				roleString = " a " + userRole.toString() + " on ";
				break;
			case ADMIN:
				roleString = " an " + userRole.toString() + " on ";
				break;
			case CREATOR:
				roleString = " the " + userRole.toString() + " of ";
				break;
		}

		// Grab the resources object
		Resources res = getActivity().getResources();

		// Set the properties
		UrlImageViewHelper.setUrlDrawable(userIcon, me.getAvatar().getNormal(),
				R.drawable.unknown_user, 60000 * 10);
		userName.setText(me.getName());
		registeredText.setText(res.getString(R.string.home_register_date_text)
				+ registeredDate);
		cloudCount.setText(res.getString(R.string.home_cloud_count_text, me
				.getClouds().size()));
		siteRole.setText(res
				.getString(R.string.home_site_role_text, roleString));
		warningCount.setText(res.getString(R.string.home_warning_count_text,
				me.getProsecutions().length));

		// Send the view back to the caller
		return homeView;
	}

}
