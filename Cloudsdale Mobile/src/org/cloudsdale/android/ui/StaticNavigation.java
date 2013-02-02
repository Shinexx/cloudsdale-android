package org.cloudsdale.android.ui;

import org.cloudsdale.android.R;

public enum StaticNavigation {

	//@formatter:off
	// ITEM("Display Name", R.drawable.ic_my_icon, "Tag")
	HOME("Home", R.drawable.ic_launcher, "home"),
	ABOUT("About", R.drawable.ic_launcher, "about");
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
