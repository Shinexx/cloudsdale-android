package org.cloudsdale.android.models.network;

import com.google.gson.annotations.SerializedName;

import org.cloudsdale.android.models.LoggedUser;
import org.cloudsdale.android.models.api.User;

public class LoginResult extends Result {

	@SerializedName("client_id")
	private String		clientId;
	private LoggedUser	user;

	/**
	 * @return the clientId
	 */
	public String getClientId() {
		return this.clientId;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * @param clientId
	 *            the clientId to set
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(LoggedUser user) {
		this.user = user;
	}
}
