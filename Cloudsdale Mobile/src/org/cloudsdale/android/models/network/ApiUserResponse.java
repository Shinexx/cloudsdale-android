package org.cloudsdale.android.models.network;

import org.cloudsdale.android.models.api.User;

public class ApiUserResponse extends ApiResponse {

	private User	result;

	public User getResult() {
		return this.result;
	}

	public void setResult(User result) {
		this.result = result;
	}

}
