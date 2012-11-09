package org.cloudsdale.android;

import org.cloudsdale.android.R;

public enum Licenses {

	//@formatter:off
	ACTIONBAR_SHERLOCK("ActionBarSherlock", R.raw.abs_license),
	FACEBOOK("facebook-android-sdk", R.raw.fb_license),
	HOLO_EVERYWHERE("HoloEverywhere", R.raw.he_license),
	SLIDING_MENU("SlidingMenu", R.raw.sm_license),
	URL_IMAGE_VIEW_HELPER("UrlImageViewHelper", R.raw.ivh_license)
	;
	//@formatter:on

	private String	mDisplayName;
	private int		mHtmlPath;

	Licenses(String displayName, int htmlPath) {
		mDisplayName = displayName;
		mHtmlPath = htmlPath;
	}

	public String getDisplayName() {
		return mDisplayName;
	}

	public int getHtmlPath() {
		return mHtmlPath;
	}

}
