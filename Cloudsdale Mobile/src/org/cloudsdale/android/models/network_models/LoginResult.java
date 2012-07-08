package org.cloudsdale.android.models.network_models;

import org.cloudsdale.android.models.LoggedUser;
import org.cloudsdale.android.models.api_models.User;

import com.google.gson.annotations.SerializedName;

public class LoginResult extends Result {

	@SerializedName("client_id")
	private String		clientId;
	private LoggedUser	user;

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(LoggedUser user) {
		this.user = user;
	}

	/**
	 * @return the clientId
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * @param clientId
	 *            the clientId to set
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
}
