package org.cloudsdale.android.models;

public enum Role {
	//@formatter:off
	NORMAL(""),
	DONOR("[ donor ]"),
	MODERATOR(""),
	CONTRIBUTOR(""),
	ADMIN(""),
	DEVELOPER("[ dev ]"),
	FOUNDER("[ founder ]")
	;
	//@formatter:on

	private String	prettyName;

	Role(String prettyName) {
		this.prettyName = prettyName;
	}

	@Override
	public String toString() {
		return this.prettyName;
	}
}
