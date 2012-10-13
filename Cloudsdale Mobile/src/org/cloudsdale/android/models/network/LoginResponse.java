package org.cloudsdale.android.models.network;

import com.google.gson.annotations.Expose;

public class LoginResponse extends Response {

	@Expose
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
