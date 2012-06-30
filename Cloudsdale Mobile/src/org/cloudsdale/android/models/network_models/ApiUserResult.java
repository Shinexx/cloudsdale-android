package org.cloudsdale.android.models.network_models;

import org.cloudsdale.android.models.api_models.User;

import com.google.gson.annotations.SerializedName;

public class ApiUserResult extends ApiResult {

	@SerializedName("user")
	private User	model;

	@Override
	public User getModel() {
		return model;
	}

	public void setModel(User model) {
		this.model = model;
	}

}
