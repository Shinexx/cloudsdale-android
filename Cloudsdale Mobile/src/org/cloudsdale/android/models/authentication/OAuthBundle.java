package org.cloudsdale.android.models.authentication;

import com.google.gson.annotations.SerializedName;

import org.cloudsdale.android.BCrypt;

public class OAuthBundle {
	private static final String	CLIENT_TYPE	= "android";

	private String				provider;
	private String				uid;
	private String				token;
	@SerializedName("client_type")
	private String				clientType;

	/**
	 * Constructor for the oAuth bundle class
	 * 
	 * @param provider
	 *            Provider for the oAuth
	 * @param uid
	 *            User's ID from provider
	 * @param token
	 *            The security token for the app
	 */
	public OAuthBundle(Provider provider, String uid, String token) {
		this.provider = provider.toString();
		this.uid = uid;
		this.token = BCrypt.hashpw(uid + provider.toString(), token);
		this.clientType = OAuthBundle.CLIENT_TYPE;
	}

	/**
	 * @return the clientType
	 */
	public String getClientType() {
		return this.clientType;
	}

	/**
	 * @return the provider
	 */
	public String getProvider() {
		return this.provider;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return this.token;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return this.uid;
	}

	/**
	 * @param clientType
	 *            the clientType to set
	 */
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	/**
	 * @param provider
	 *            the provider to set
	 */
	public void setProvider(String provider) {
		this.provider = provider;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @param uid
	 *            the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
}
