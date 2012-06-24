package org.cloudsdale.android.models;

public enum Role {
	NORMAL("normal user"),
	DONOR("donor"),
	MODERATOR("moderator"),
	PLACEHOLDER("placeholder"),
	ADMIN("admin"),
	CREATOR("creator");
	
	private String prettyName;
	
	Role(String prettyName) {
		this.prettyName = prettyName;
	}
	
	@Override
	public String toString() {
		return prettyName;
	}
}
