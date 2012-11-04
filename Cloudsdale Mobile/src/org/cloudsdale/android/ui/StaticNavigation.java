package org.cloudsdale.android.ui;

import org.cloudsdale.android.R;
import org.cloudsdale.android.R.drawable;

public enum StaticNavigation {

	HOME("Home", R.drawable.ic_action_home, "Home"), SETTINGS("Settings",
			R.drawable.ic_action_settings, "Settings"), EXPLORE("Explore",
			R.drawable.ic_action_globe, "Explore"), LOGOUT("Sign out",
			R.drawable.ic_action_exit, "Logout");

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
