package org.cloudsdale.android.models.queries;

import android.content.Context;
import android.util.Log;

import com.bugsense.trace.BugSenseHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.cloudsdale.android.models.LoggedUser;
import org.cloudsdale.android.models.Model;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.annotations.GsonIgnoreExclusionStrategy;
import org.cloudsdale.android.models.exceptions.ExternalServiceException;
import org.cloudsdale.android.models.exceptions.NotAuthorizedException;
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

	public SessionQuery(String url) {
        super(url);
    }

    private static final String	TAG	= "Session Query";

	private String				mJsonString;
	private LoggedUser			mUser;

	/**
	 * Execute the query, establishing a session with Cloudsdale
	 * 
	 * @throws NotAuthorizedException
	 * @throws ExternalServiceException
	 */
	@Override
	public LoggedUser execute(QueryData data, Context context) {
		// Set the entity
		if (data.getHeaders() != null) {
			try {
				this.mHttpPost.setEntity(new UrlEncodedFormEntity(data
						.getHeaders()));
			} catch (UnsupportedEncodingException e) {
				BugSenseHandler.log(SessionQuery.TAG, e);
			}
		} else if (data.getJson() != null) {
			try {
				this.mHttpPost.setEntity(new StringEntity(data.getJson()));
			} catch (UnsupportedEncodingException e) {
				BugSenseHandler.log(SessionQuery.TAG, e);
			}
		}

		// Query the API
		try {
			// Get the response
			mHttpResponse = mhttpClient.execute(mHttpPost);

			if (mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
				return null;
			} else if (String.valueOf(
					this.mHttpResponse.getStatusLine().getStatusCode())
					.startsWith("5")) { return null; }

			// Build the json
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					this.mHttpResponse.getEntity().getContent(), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null;) {
				sb.append(line);
			}
			mJsonString = sb.toString();
			mJsonString = stripHtml(mJsonString);

			// [DEBUG] Logcat the json response
			Log.d(SessionQuery.TAG, "Session API response: " + mJsonString);

			// Deserialize
			GsonBuilder gb = new GsonBuilder();
			gb.setExclusionStrategies(new GsonIgnoreExclusionStrategy(
					String[].class));
			Gson gson = gb.create();
			if (this.mJsonString != null) {
				LoginResponse resp = gson.fromJson(mJsonString,
						LoginResponse.class);
				mUser = (LoggedUser) resp.getResult()
						.getUser();
				mUser.setClientId(resp.getResult()
						.getClientId());
			}
		} catch (ClientProtocolException e) {
			Log.d(SessionQuery.TAG, "Client Protocol Exception");
			BugSenseHandler.log(SessionQuery.TAG, e);
		} catch (IOException e) {
			Log.d(SessionQuery.TAG, "IO Exception");
			BugSenseHandler.log(SessionQuery.TAG, e);
		}

		Log.d(SessionQuery.TAG, "User: "
				+ (this.mUser == null ? "Null" : "Not Null"));

		return this.mUser;
	}

	/**
	 * Not implemented for sessions, will always return null. Use
	 * {@link #execute(QueryData, Context)} instead
	 */
	@Deprecated
	@Override
	public Model[] executeForCollection(QueryData data, Context context) {
		// Stub, will never be used in this class
		return null;
	}
}
