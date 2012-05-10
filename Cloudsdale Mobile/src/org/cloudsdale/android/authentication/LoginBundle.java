package org.cloudsdale.android.authentication;

/**
 * Bundle to store all the objects required for login
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class LoginBundle {
	private String	usernameInput;
	private String	passwordInput;
	private String	loginUrl;

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
	public LoginBundle(String username, String password, String loginUrl) {
		this.usernameInput = username;
		this.passwordInput = password;
		this.loginUrl = loginUrl;
	}

	/**
	 * Get the user's username
	 * 
	 * @return The user's username
	 */
	public String getUsernameInput() {
		return usernameInput;
	}

	/**
	 * Get the user's password
	 * 
	 * @return The user's password
	 */
	public String getPasswordInput() {
		return passwordInput;
	}

	/**
	 * Get the login url
	 * 
	 * @return The url to log in to
	 */
	public String getLoginUrl() {
		return loginUrl;
	}
}
