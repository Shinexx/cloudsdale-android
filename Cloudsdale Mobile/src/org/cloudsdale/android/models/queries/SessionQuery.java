package org.cloudsdale.android.models.queries;

import android.util.Log;

import com.bugsense.trace.BugSenseHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.cloudsdale.android.models.LoggedUser;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.annotations.GsonIgnoreExclusionStrategy;
import org.cloudsdale.android.models.network_models.LoginResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Query runner class that asynchronously establishes a session with Cloudsdale.
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com) Copyright (C) 2012
 *         Cloudsdale.org, all rights reserved
 */
public class SessionQuery extends PostQuery {

	private static final String	TAG	= "Session Query";

	private String				json;
	private LoggedUser			user;

	/**
	 * Execute the query, establishing a session with Cloudsdale
	 */
	@Override
	public LoggedUser execute(final QueryData data) {
		// Mark the query as alive
		isAlive = true;

		// Build the HTTP objects
		this.setupHttpObjects(data.getUrl());

		// Set the entity
		if (data.getHeaders() != null) {
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(data.getHeaders()));
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

			// If we got anything other than 200 OK, break
			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) { throw new Exception(
					"Server query failed with error code "
							+ httpResponse.getStatusLine().getStatusCode()); }

			// Build the json
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpResponse.getEntity().getContent(), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null;) {
				sb.append(line);
			}
			json = sb.toString();
			json = stripHtml(json);

			// [DEBUG] Logcat the json response
			Log.d(TAG, "Session API response: " + json);

			// Deserialize
			GsonBuilder gb = new GsonBuilder();
			gb.setExclusionStrategies(new GsonIgnoreExclusionStrategy(
					String[].class));
			Gson gson = gb.create();
			if (json != null) {
				LoginResponse resp = gson.fromJson(json, LoginResponse.class);
				SessionQuery.this.user = (LoggedUser) resp.getResult()
						.getUser();
				SessionQuery.this.user.setClientId(resp.getResult()
						.getClientId());
			}
		} catch (ClientProtocolException e) {
			Log.d(TAG, "Client Protocol Exception");
			BugSenseHandler.log(TAG, e);
		} catch (IOException e) {
			Log.d(TAG, "IO Exception");
			BugSenseHandler.log(TAG, e);
		} catch (Exception e) {
			Log.d(TAG, "General Exception caught: " + e.getMessage());
			BugSenseHandler.log(TAG, e);
		}

		Log.d(TAG, "User: " + (user == null ? "Null" : "Not Null"));

		return user;
	}
}
