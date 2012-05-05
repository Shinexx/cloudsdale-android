package org.cloudsdale.android.authentication;

import org.cloudsdale.android.logic.PersistantData;

import android.app.Activity;

public class LoginBundle {
	private PersistantData	dataObject;
	private String			usernameInput;
	private String			passwordInput;
	private String			loginUrl;

	public LoginBundle(PersistantData data, String username, String password,
			String loginUrl) {
		this.dataObject = data;
		this.usernameInput = username;
		this.passwordInput = password;
		this.loginUrl = loginUrl;
	}

	public PersistantData getData() {
		return dataObject;
	}

	public String getUsernameInput() {
		return usernameInput;
	}

	public String getPasswordInput() {
		return passwordInput;
	}

	public String getLoginUrl() {
		return loginUrl;
	}
}
