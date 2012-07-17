package org.cloudsdale.android.models.network_models;

public class ApiUserResponse extends ApiResponse {

	private ApiUserResult	result;

	@Override
	public ApiUserResult getResult() {
		return this.result;
	}

	public void setResult(ApiUserResult result) {
		this.result = result;
	}

}
