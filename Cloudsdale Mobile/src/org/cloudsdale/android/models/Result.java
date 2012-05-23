package org.cloudsdale.android.models;

import com.google.gson.annotations.SerializedName;

public class Result {
	@SerializedName("client_id")
	protected String clientId;

	/**
	 * @return the clientId
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
}
