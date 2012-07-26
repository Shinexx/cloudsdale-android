package org.cloudsdale.android.models.queries;

import android.content.Context;
import android.util.Log;

import com.bugsense.trace.BugSenseHandler;
import com.google.gson.Gson;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.api_models.User;
import org.cloudsdale.android.models.network_models.ApiUserResponse;

import java.io.IOException;

public class UserGetQuery extends GetQuery {

	private static final String	TAG	= "UserGet Query";

	private String				json;
	private User				u;
	private ApiUserResponse		response;

	/**
	 * Executes the query using data provided to return a User from the
	 * Cloudsdale API
	 * 
	 * @param data
	 *            The object containing all the header data required by the
	 *            query
	 * @param context
	 *            Not used in this query implementation
	 */
	@Override
	public User execute(final QueryData data, final Context context) {
		// Mark the query as alive
		this.isAlive = true;

		// Setup HTTP components
		setupHttpObjects(data.getUrl());

		// Set the headers
		if (data.getHeaders() != null) {
			for (BasicNameValuePair nvp : data.getHeaders()) {
				if (nvp.getName().toLowerCase().equals("x-auth-token")) {
					UserGetQuery.this.httpGet.setHeader(nvp.getName(),
							nvp.getValue());
				}
			}
		}

		// Query the API
		try {
			// Get the response
			this.httpResponse = this.httpClient.execute(this.httpGet);

			// If we got anything other than a user, break out, there's
			// no point to continuing
			if (this.httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) { return null; }

			// Build the json
			this.json = EntityUtils.toString(UserGetQuery.this.httpResponse
					.getEntity());
			this.json = stripHtml(UserGetQuery.this.json);

			// Deserialize
			Gson gson = new Gson();
			if (this.json != null) {
				Log.d(TAG, this.json);
				this.response = gson.fromJson(this.json,
						ApiUserResponse.class);
				this.u = this.response.getResult();
			}
		} catch (ClientProtocolException e) {
			BugSenseHandler.log(UserGetQuery.TAG, e);
		} catch (IOException e) {
			BugSenseHandler.log(UserGetQuery.TAG, e);
		}

		return this.u;
	}
}
