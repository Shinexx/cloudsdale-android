package org.cloudsdale.android.models.network_models;

public class ApiUserResponse extends ApiResponse {
	
	private ApiUserResult result;

	public ApiUserResult getResult() {
		return result;
	}

	public void setResult(ApiUserResult result) {
		this.result = result;
	}

}
