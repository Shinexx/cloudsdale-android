package org.cloudsdale.android.models;

import org.cloudsdale.android.models.api_models.User;

public class LoggedUser extends User {
	
	private String clientId;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

}