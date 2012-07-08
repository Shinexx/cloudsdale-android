package org.cloudsdale.android.models.queries;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.cloudsdale.android.models.LoggedUser;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.annotations.GsonIgnoreExclusionStrategy;
import org.cloudsdale.android.models.network_models.LoginResponse;

import android.util.Log;

import com.bugsense.trace.BugSenseHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SessionQuery extends PostQuery {

	private static final String	TAG	= "Session Query";

	private String				json;
	private LoggedUser			user;

	@Override
	public LoggedUser execute(final QueryData data) {

		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// Create the objects
				setupHttpObjects(data.getUrl());

				// Set the entity
				if (data.getHeaders() != null) {
					try {
						httpPost.setEntity(new UrlEncodedFormEntity(data
								.getHeaders()));
					} catch (UnsupportedEncodingException e) {
						BugSenseHandler.log(TAG, e);
					}
				} else if (data.getJson() != null) {
					try {
						httpPost.setEntity(new StringEntity(data.getJson()));
					} catch (UnsupportedEncodingException e) {
						BugSenseHandler.log(TAG, e);
					}
				}

				// Query the API
				try {
					// Get the response
					httpResponse = httpClient.execute(httpPost);

					// If we got anything other than a user, break out, there's
					// no point to continuing
					if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) { return; }

					// Build the json
					json = EntityUtils.toString(httpResponse.getEntity());
					json = stripHtml(json);

					Log.d(TAG, "Session API response: " + json);

					// Deserialize
					GsonBuilder gb = new GsonBuilder();
					gb.setExclusionStrategies(new GsonIgnoreExclusionStrategy(
							String[].class));
					Gson gson = gb.create();
					if (json != null) {
						LoginResponse resp = gson.fromJson(json,
								LoginResponse.class);
						SessionQuery.this.user = (LoggedUser) resp.getResult()
								.getUser();
						SessionQuery.this.user.setClientId(resp.getResult()
								.getClientId());
					}
				} catch (ClientProtocolException e) {
					BugSenseHandler.log(TAG, e);
				} catch (IOException e) {
					BugSenseHandler.log(TAG, e);
				}
			}
		});
		thread.start();

		while (thread.isAlive()) {
			continue;
		}

		Log.d(TAG, "User: " + (user == null ? "Null" : "Not Null"));

		return user;
	}

}
