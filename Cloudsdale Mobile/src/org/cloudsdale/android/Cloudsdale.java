package org.cloudsdale.android;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.slidingmenu.lib.SlidingMenu;

import org.cloudsdale.android.models.api_models.User;
import org.cloudsdale.android.ui.adapters.CloudsAdapter;

/**
 * Global application class
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class Cloudsdale extends Application {

	public static int dpToPx(int dp, Context ctx) {
		Resources r = ctx.getResources();
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				r.getDisplayMetrics());
	}

	// originally:
	// http://stackoverflow.com/questions/5418510/disable-the-touch-events-for-all-the-views
	// modified for the needs here
	public static void enableDisableViewGroup(ViewGroup viewGroup,
			boolean enabled) {
		int childCount = viewGroup.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View view = viewGroup.getChildAt(i);
			if (view.isFocusable()) {
				view.setEnabled(enabled);
			}
			if (view instanceof ViewGroup) {
				Cloudsdale.enableDisableViewGroup((ViewGroup) view, enabled);
			} else if (view instanceof ListView) {
				if (view.isFocusable()) {
					view.setEnabled(enabled);
				}
				ListView listView = (ListView) view;
				int listChildCount = listView.getChildCount();
				for (int j = 0; j < listChildCount; j++) {
					if (view.isFocusable()) {
						listView.getChildAt(j).setEnabled(false);
					}
				}
			}
		}
	}

	/**
	 * Dummy constructor to handle creating static classes and fetch the global
	 * app context
	 */
	public Cloudsdale() {
		super();
		new PersistentData();
	}

	public static void prepareSlideMenu(SlidingMenu slidingMenu,
			Activity context) {
		// View settings
		slidingMenu.showAbove();
		slidingMenu.setBehindOffsetRes(R.dimen.actionbar_home_width);

		// Get all the layout items
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View head = inflater.inflate(R.layout.menu_header, null);
		ListView itemOptions = (ListView) slidingMenu
				.findViewById(R.id.slide_menu_list);

		User me = PersistentData.getMe(context);
		setSlideMenuHeader(head, itemOptions, me);
		addStaticSlideMenuViews(itemOptions, inflater);
		itemOptions.setAdapter(new CloudsAdapter(context, me.getClouds()));
	}

	private static void setSlideMenuHeader(View head, ListView itemOptions,
			User me) {
		itemOptions.addHeaderView(head, null, false);
		itemOptions.setHeaderDividersEnabled(true);

		ImageView userIcon = (ImageView) head
				.findViewById(R.id.slide_menu_user_icon);
		TextView userName = (TextView) head
				.findViewById(R.id.slide_menu_username_label);

		UrlImageViewHelper.setUrlDrawable(userIcon, me.getAvatar().getNormal(),
				R.drawable.unknown_user, 30 * 60 * 1000);
		userName.setText(me.getName());
		head.setClickable(false);
	}

	private static void addStaticSlideMenuViews(ListView list,
			LayoutInflater inflater) {
		View homeView = inflater.inflate(R.layout.cloud_list_item, null);
		View settingsView = inflater.inflate(R.layout.cloud_list_item, null);
		View logoutView = inflater.inflate(R.layout.cloud_list_item, null);

		ImageView homeIcon = (ImageView) homeView.findViewById(R.id.cloud_icon);
		homeIcon.setImageResource(R.drawable.color_icon);
		TextView homeText = (TextView) homeView.findViewById(R.id.cloud_name);
		homeText.setText("Home");
		list.addHeaderView(homeView);

		ImageView settingsIcon = (ImageView) settingsView
				.findViewById(R.id.cloud_icon);
		settingsIcon.setImageResource(R.drawable.color_icon);
		TextView settingsText = (TextView) settingsView
				.findViewById(R.id.cloud_name);
		settingsText.setText("Settings");
		list.addHeaderView(settingsView);

		ImageView logoutIcon = (ImageView) logoutView
				.findViewById(R.id.cloud_icon);
		logoutIcon.setImageResource(R.drawable.color_icon);
		TextView logoutText = (TextView) logoutView
				.findViewById(R.id.cloud_name);
		logoutText.setText("Log out");
		list.addHeaderView(logoutView);
	}
}
