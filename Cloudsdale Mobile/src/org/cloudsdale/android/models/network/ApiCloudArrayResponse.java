package org.cloudsdale.android.models.network;

import org.cloudsdale.android.models.api.Cloud;

public class ApiCloudArrayResponse extends ApiResponse {

	private Cloud[]	result;

	public Cloud[] getResult() {
		return this.result;
	}

	public void setResult(Cloud[] result) {
		this.result = result;
	}

}
