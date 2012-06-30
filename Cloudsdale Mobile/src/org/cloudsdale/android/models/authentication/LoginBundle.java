package org.cloudsdale.android.models.authentication;


/**
 * Bundle to store all the objects required for login
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class LoginBundle {
	private String		usernameInput;
	private String		passwordInput;
	private String		loginUrl;
	private String		authToken;
	private OAuthBundle	oAuthBundle;

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

	/**
	 * Get the internal authorization token
	 * 
	 * @return The internal authorization token string
	 */
	public String getAuthToken() {
		return authToken;
	}

	/**
	 * @return the oAuthBundle
	 */
	public OAuthBundle getoAuthBundle() {
		return oAuthBundle;
	}

	/**
	 * @param oAuthBundle
	 *            the Name/Value pair bundle to set
	 */
	public void setoAuthBundle(OAuthBundle oAuthBundle) {
		this.oAuthBundle = oAuthBundle;
	}
}
