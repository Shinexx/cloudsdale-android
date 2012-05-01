package org.cloudsdale.android.logic;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class CloudsdaleAsyncQueryWrapper {
	public static final String	TAG	= "CloudsdaleAsyncQueryWrapper";
	private JSONObject			queriedObject;

	/**
	 * Query Cloudsdale to get an object
	 * 
	 * @param <T>
	 *            Type to return
	 * 
	 * @param params
	 * @return
	 */
	public <T> T query(String[] params, Class<T> returnType, String objectMarker) {
		CloudsdaleAsyncQueryParams _params = new CloudsdaleAsyncQueryParams(
				this, params);
		new CloudsdaleAsyncGetQuery().execute(_params);

		T result = null;

		try {
			Gson gson = new Gson();
			result = gson.fromJson(queriedObject.getString(objectMarker),
					returnType);
		} catch (JsonSyntaxException e) {
			Log.e(TAG, "JsonSyntaxException -- " + e.getMessage());
		} catch (JSONException e) {
			Log.e(TAG, "JSONException -- " + e.getMessage());
		}

		return result;
	}

	public void postExecute(JSONObject result) {
		queriedObject = result;
	}
}
