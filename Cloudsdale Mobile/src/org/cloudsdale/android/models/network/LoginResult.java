package org.cloudsdale.android.models.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.cloudsdale.android.models.LoggedUser;
import org.cloudsdale.android.models.api.User;

public class LoginResult extends Result {

	@Expose
	@SerializedName("client_id")
	private String		mClientId;
	@Expose
	@SerializedName("user")
	private LoggedUser	mUser;

	/**
	 * @return the clientId
	 */
	public String getClientId() {
		return this.mClientId;
	}

	/**
	 * @return the user
	 */
	public LoggedUser getUser() {
		return this.mUser;
	}

	/**
	 * @param clientId
	 *            the clientId to set
	 */
	public void setClientId(String clientId) {
		this.mClientId = clientId;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(LoggedUser user) {
		this.mUser = user;
	}
}
