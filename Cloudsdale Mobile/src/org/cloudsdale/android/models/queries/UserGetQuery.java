package org.cloudsdale.android.models.queries;

import android.content.Context;
import android.util.Log;

import com.bugsense.trace.BugSenseHandler;
import com.google.gson.Gson;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.cloudsdale.android.models.Model;
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
		isAlive = true;

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
			json = EntityUtils.toString(httpResponse
					.getEntity());
			json = stripHtml(json);

			// Deserialize
			Gson gson = new Gson();
			if (json != null) {
				Log.d(TAG, json);
				response = gson.fromJson(json,
						ApiUserResponse.class);
				u = response.getResult();
			}
		} catch (ClientProtocolException e) {
			BugSenseHandler.log(TAG, e);
		} catch (IOException e) {
			BugSenseHandler.log(TAG, e);
		}

		return u;
	}

	@Override
	public Model[] executeForCollection(QueryData data, Context context) {
		//  TODO NYI
		return null;
	}
}
