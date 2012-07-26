package org.cloudsdale.android.models.network_models;

import org.cloudsdale.android.models.api_models.User;

public class ApiUserResponse extends ApiResponse {

	private User	result;

	public User getResult() {
		return this.result;
	}

	public void setResult(User result) {
		this.result = result;
	}

}
