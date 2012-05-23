package org.cloudsdale.android.authentication;

public enum Provider {
	FACEBOOK("facebook"),
	TWITTER("twitter"),
	CLOUDSDALE("cloudsdale");
	
	private String displayName;
	
	Provider(String displayName) {
		this.displayName = displayName;
	}
	
	public String toString() {
		return displayName;
	}
}
