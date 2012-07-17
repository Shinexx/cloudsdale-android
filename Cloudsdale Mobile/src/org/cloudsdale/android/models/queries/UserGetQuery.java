package org.cloudsdale.android.models.queries;

import android.content.Context;

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

	@Override
	public User execute(final QueryData data, final Context context) {
		// Mark the query as alive
		this.isAlive = true;

		// Setup HTTP components
		setupHttpObjects(data.getUrl());

		new Thread(new Runnable() {

			@Override
			public void run() {

				// Set the headers
				if (data.getHeaders() != null) {
					for (BasicNameValuePair nvp : data.getHeaders()) {
						if (nvp.getName().equals("X-Auth-Token")) {
							UserGetQuery.this.httpGet.setHeader(nvp.getName(),
									nvp.getValue());
						}
					}
				}

				// Query the API
				try {
					// Get the response
					UserGetQuery.this.httpResponse = UserGetQuery.this.httpClient
							.execute(UserGetQuery.this.httpGet);

					// If we got anything other than a user, break out, there's
					// no point to continuing
					if (UserGetQuery.this.httpResponse.getStatusLine()
							.getStatusCode() != HttpStatus.SC_OK) { return; }

					// Build the json
					UserGetQuery.this.json = EntityUtils
							.toString(UserGetQuery.this.httpResponse
									.getEntity());
					UserGetQuery.this.json = stripHtml(UserGetQuery.this.json);

					// Deserialize
					Gson gson = new Gson();
					if (UserGetQuery.this.json != null) {
						ApiUserResponse resp = gson.fromJson(
								UserGetQuery.this.json, ApiUserResponse.class);
						UserGetQuery.this.u = resp.getResult().getModel();
					}
				} catch (ClientProtocolException e) {
					BugSenseHandler.log(UserGetQuery.TAG, e);
				} catch (IOException e) {
					BugSenseHandler.log(UserGetQuery.TAG, e);
				}
			}
		}).start();

		return this.u;
	}
}
