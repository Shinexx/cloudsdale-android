package org.cloudsdale.android.authentication;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class OAuthBundle {
	private static final String	CLIENT_TYPE	= "android";

	private String				provider;
	private String				uid;
	private String				token;
	@SerializedName("client_type")
	private String				clientType;

	public OAuthBundle(Provider provider, String uid, String token) {
		this.provider = provider.toString();
		this.uid = uid;
		this.token = token;
		clientType = CLIENT_TYPE;
	}

	public String toString() {
		return new Gson().toJson(this);
	}

	/**
	 * @return the provider
	 */
	public String getProvider() {
		return provider;
	}

	/**
	 * @param provider the provider to set
	 */
	public void setProvider(String provider) {
		this.provider = provider;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the clientType
	 */
	public String getClientType() {
		return clientType;
	}

	/**
	 * @param clientType the clientType to set
	 */
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
}
