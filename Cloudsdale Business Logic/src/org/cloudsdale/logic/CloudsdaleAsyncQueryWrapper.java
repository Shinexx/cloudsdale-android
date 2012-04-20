package org.cloudsdale.logic;

import org.json.JSONObject;

public class CloudsdaleAsyncQueryWrapper {
	private JSONObject queriedObject;
	
	public JSONObject query(String[] params) {
		CloudsdaleAsyncQueryParams _params = new CloudsdaleAsyncQueryParams(this, params);
		new CloudsdaleAsyncGetQuery().execute(_params);
		return queriedObject;
	}
	
	public void postExecute(JSONObject result) {
		queriedObject = result;
	}
}
