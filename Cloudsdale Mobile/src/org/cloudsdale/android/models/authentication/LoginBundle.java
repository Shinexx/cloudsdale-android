package org.cloudsdale.android.models.authentication;

import android.content.Context;

/**
 * Bundle to store all the objects required for login
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class LoginBundle {
	private String		usernameInput;
	private String		passwordInput;
	private String		loginUrl;
	private String		authToken;
	private OAuthBundle	oAuthBundle;
	private Context		context;

	/**
	 * Constructor
	 * 
	 * @param username
	 *            The user's entered username
	 * @param password
	 *            The user's entered password
	 * @param loginUrl
	 *            The url to login to
	 */
	public LoginBundle(String username, String password, String loginUrl,
			String authToken, OAuthBundle oAuthBundle) {
		this.usernameInput = username;
		this.passwordInput = password;
		this.loginUrl = loginUrl;
		this.authToken = authToken;
		this.oAuthBundle = oAuthBundle;
	}

	/**
	 * Get the internal authorization token
	 * 
	 * @return The internal authorization token string
	 */
	public String getAuthToken() {
		return this.authToken;
	}

	/**
	 * Get the login url
	 * 
	 * @return The url to log in to
	 */
	public String getLoginUrl() {
		return this.loginUrl;
	}

	/**
	 * @return the oAuthBundle
	 */
	public OAuthBundle getoAuthBundle() {
		return this.oAuthBundle;
	}

	/**
	 * Get the user's password
	 * 
	 * @return The user's password
	 */
	public String getPasswordInput() {
		return this.passwordInput;
	}

	/**
	 * Get the user's username
	 * 
	 * @return The user's username
	 */
	public String getUsernameInput() {
		return this.usernameInput;
	}

	/**
	 * @param oAuthBundle
	 *            the Name/Value pair bundle to set
	 */
	public void setoAuthBundle(OAuthBundle oAuthBundle) {
		this.oAuthBundle = oAuthBundle;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
}
