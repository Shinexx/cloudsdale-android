package org.cloudsdale.android.models.network_models;


public class LoginResponse extends Response {

	private LoginResult result;
	
	/**
	 * @return the result
	 */
	@Override
	public LoginResult getResult() {
		return result;
	}
	
	/**
	 * @param result the result to set
	 */
	public void setResult(LoginResult result) {
		this.result = result;
	}
}
