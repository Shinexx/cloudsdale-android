package org.cloudsdale.android.models.network;

import com.google.gson.annotations.Expose;

import org.cloudsdale.android.models.api.User;

public class ApiUserResult {

	@Expose
	private User	result;

	public User getResult() {
		return this.result;
	}

	public void setResult(User result) {
		this.result = result;
	}

}
