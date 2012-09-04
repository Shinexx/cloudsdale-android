package org.cloudsdale.android.models;

public enum Role {
	NORMAL(""), DONOR("donor"), MODERATOR(""), PLACEHOLDER(
			""), ADMIN(""), DEVELOPER("[ dev ]"), FOUNDER("[ founder ]");

	private String	prettyName;

	Role(String prettyName) {
		this.prettyName = prettyName;
	}

	@Override
	public String toString() {
		return this.prettyName;
	}
}
