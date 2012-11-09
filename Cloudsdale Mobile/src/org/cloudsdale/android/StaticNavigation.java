package org.cloudsdale.android;

import org.cloudsdale.android.R;

public enum StaticNavigation {

	//@formatter:off
	HOME("Home", R.drawable.ic_action_home, "Home"), 
	SETTINGS("Settings", R.drawable.ic_action_settings, "Settings"), 
	EXPLORE("Explore", R.drawable.ic_action_globe, "Explore"), 
	LOGOUT("Sign out", R.drawable.ic_action_exit, "Logout"),
	ABOUT("About Cloudsdale", R.drawable.ic_color_icon, "About")
	;
	//@formatter:on

	private String	displayName;
	private int		resId;
	private String	textId;

	private StaticNavigation(String display, int resid, String textid) {
		this.displayName = display;
		this.resId = resid;
		this.textId = textid;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getResId() {
		return resId;
	}

	public String getTextId() {
		return textId;
	}

}
