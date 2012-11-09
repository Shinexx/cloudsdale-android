package org.cloudsdale.android;

public enum Contributers {

	//@formatter:off
	ZEERAW("Zeeraw", "zeeraw", "Founder"), 
	LISINGE("Lisinge", "lisinge", "Server Technician"), 
	AETHE("Aethe", "aethe", "iOS Developer"), 
	BERWYN("Berwyn", "berwyn", "Android Developer"), 
	CONNOR("Connorcpu", "connorcpu", "Windows Developer"), 
	ZIMBER("Zimber Fizz", "zimber","Graphics Designer"), 
	MANEARION("Manearion", "mane", "Art Director"), 
	XTUX("Xtux", "xtux", "Optimization and SEO"), 
	MATT("Rainbow Dash", "dash", "Administrator"), 
	SHAN("Shansai", "shan", "Moderator"), 
	WANE("Wanew", "wanew", "Moderator"), 
	NITRO("Nitro", null, "Moderator"), 
	JAK("Jakben Imbel", null, "Moderator"), 
	DRIC("Dricanus", null, "Moderator"), 
	NIFE("Nife", null, "Moderator"), 
	BEAN("Green Bean", null, "Moderator"), 
	WINTER("Winter Storm", null, "Moderator"),
	SIX("Six Reader", null, "Former Moderator"),
	DUSK("Dawn Blush", null, "Former Moderator")
	;	// @formatter:on

	private static final String	URL_BASE				= "http://assets.cloudsdale.org/assets/contributors/";
	// Three days
	public static final long	IMAGE_EXPIRATION_MILLIS	= 1000 * 60 * 60 * 24
																* 3;

	private String				mDisplayName;
	private String				mWebSafeName;
	private String				mTitle;

	Contributers(String displayName, String webSafeName, String title) {
		mDisplayName = displayName;
		mWebSafeName = webSafeName;
		mTitle = title;
	}

	public String getDisplayName() {
		return mDisplayName;
	}

	public String getImageUrl() {
		if (mWebSafeName != null) {
			return URL_BASE + mWebSafeName + ".png";
		} else return mWebSafeName;
	}

	public String getTitle() {
		return mTitle;
	}
}
