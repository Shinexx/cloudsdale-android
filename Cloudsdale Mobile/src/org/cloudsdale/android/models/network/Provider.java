package org.cloudsdale.android.models.network;

public enum Provider {
	//@formatter:off
	FACEBOOK("facebook"), 
	TWITTER("twitter"), 
	CLOUDSDALE("cloudsdale")
	;
	//@formatter:on

	private String	displayName;

	Provider(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return this.displayName;
	}
}
