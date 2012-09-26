package org.cloudsdale.android.models.network;

public class LoginResponse extends Response {

	private LoginResult	result;

	/**
	 * @return the result
	 */
	public LoginResult getResult() {
		return this.result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(LoginResult result) {
		this.result = result;
	}
}
