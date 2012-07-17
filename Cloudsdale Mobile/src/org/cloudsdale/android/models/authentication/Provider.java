package org.cloudsdale.android.models.authentication;

public enum Provider {
	FACEBOOK("facebook"), TWITTER("twitter"), CLOUDSDALE("cloudsdale");

	private String	displayName;

	Provider(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return this.displayName;
	}
}
