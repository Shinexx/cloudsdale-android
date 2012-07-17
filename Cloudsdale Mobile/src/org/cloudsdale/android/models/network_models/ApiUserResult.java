package org.cloudsdale.android.models.network_models;

import com.google.gson.annotations.SerializedName;

import org.cloudsdale.android.models.api_models.User;

public class ApiUserResult extends ApiResult {

	@SerializedName("user")
	private User	model;

	@Override
	public User getModel() {
		return this.model;
	}

	public void setModel(User model) {
		this.model = model;
	}

}
