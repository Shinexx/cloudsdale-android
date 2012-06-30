package org.cloudsdale.android.models.queries;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.api_models.User;
import org.cloudsdale.android.models.network_models.ApiUserResponse;

import com.bugsense.trace.BugSenseHandler;
import com.google.gson.Gson;

public class UserGetQuery extends GetQuery {

	private static final String	TAG	= "UserGet Query";

	private String				json;
	private User				u;

	@Override
	public User execute(final QueryData data) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// Setup HTTP components
				setupHttpObjects(data.getUrl());

				// Set parameters
				try {
					httpGet.setParams((HttpParams) new UrlEncodedFormEntity(
							data.getHeaders()));
				} catch (UnsupportedEncodingException e) {
					BugSenseHandler.log(TAG, e);
				}

				// Query the API
				try {
					// Get the response
					httpResponse = httpClient.execute(httpGet);

					// If we got anything other than a user, break out, there's
					// no point to continuing
					if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) { return; }

					// Build the json
					json = EntityUtils.toString(httpResponse.getEntity());

					// Deserialize
					Gson gson = new Gson();
					if (json != null) {
						ApiUserResponse resp = gson.fromJson(json,
								ApiUserResponse.class);
						u = resp.getResult().getModel();
					}
				} catch (ClientProtocolException e) {
					BugSenseHandler.log(TAG, e);
				} catch (IOException e) {
					BugSenseHandler.log(TAG, e);
				}
			}
		}).start();

		return u;
	}

}
