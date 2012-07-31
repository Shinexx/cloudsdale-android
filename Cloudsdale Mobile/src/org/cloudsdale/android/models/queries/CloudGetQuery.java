package org.cloudsdale.android.models.queries;

import android.content.Context;
import android.util.Log;

import com.bugsense.trace.BugSenseHandler;
import com.google.gson.Gson;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.api_models.Cloud;
import org.cloudsdale.android.models.network_models.ApiCloudArrayResponse;

import java.io.IOException;

public class CloudGetQuery extends GetQuery {

	private static final String	TAG	= "Cloud Get Query";

	private String				json;
	private Cloud[]				results;
	private Cloud				result;

	@Override
	public Cloud[] executeForCollection(QueryData data, Context context) {
		setupHttpObjects(data.getUrl());
		setHeaders(data.getHeaders());

		// Query the API
		try {
			// Get the response
			httpResponse = httpClient.execute(httpGet);

			// If we got anything other than a user, break out, there's
			// no point to continuing
			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) { return null; }

			// Build the json
			json = EntityUtils.toString(httpResponse.getEntity());
			json = stripHtml(json);

			// Deserialize
			Gson gson = new Gson();
			Log.d(TAG, json == null ? "Json is null" : json);
			if (json != null) {
				results = gson.fromJson(json, ApiCloudArrayResponse.class)
						.getResult();
			}
		} catch (ClientProtocolException e) {
			BugSenseHandler.log(TAG, e);
		} catch (IOException e) {
			BugSenseHandler.log(TAG, e);
		}

		return results;
	}

	@Override
	public Cloud execute(QueryData data, Context context) {
		setupHttpObjects(data.getUrl());
		setHeaders(data.getHeaders());

		// Query the API
		try {
			// Get the response
			httpResponse = httpClient.execute(httpGet);

			// If we got anything other than a user, break out, there's
			// no point to continuing
			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) { return null; }

			// Build the json
			json = EntityUtils.toString(httpResponse.getEntity());
			json = stripHtml(json);

			// Deserialize
			Gson gson = new Gson();
			if (json != null) {
				Log.d(TAG, json);
				result = gson.fromJson(json, Cloud.class);
			}
		} catch (ClientProtocolException e) {
			BugSenseHandler.log(TAG, e);
		} catch (IOException e) {
			BugSenseHandler.log(TAG, e);
		}

		return result;
	}

}
