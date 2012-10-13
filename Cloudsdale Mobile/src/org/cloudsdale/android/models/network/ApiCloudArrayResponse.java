package org.cloudsdale.android.models.network;

import com.google.gson.annotations.Expose;

import org.cloudsdale.android.models.api.Cloud;

public class ApiCloudArrayResponse extends ApiResponse {

	@Expose
	private Cloud[]	result;

	public Cloud[] getResult() {
		return this.result;
	}

	public void setResult(Cloud[] result) {
		this.result = result;
	}

}
