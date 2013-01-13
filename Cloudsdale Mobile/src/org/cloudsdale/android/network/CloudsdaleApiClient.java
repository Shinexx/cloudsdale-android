package org.cloudsdale.android.network;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;
import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.network.Provider;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class CloudsdaleApiClient {

	private static String	BASE_URL;
	private static String	SESSIONS_ENDPOINT;
	private static String	USERS_ENDPOINT;
	private static String	USERS_CLOUDS_ENDPOINT;

	static {
		Context appContext = Cloudsdale.getContext();
		BASE_URL = appContext.getString(R.string.cloudsdale_api_base);
		SESSIONS_ENDPOINT = appContext
				.getString(R.string.cloudsdale_sessions_endpoint);
		USERS_ENDPOINT = appContext
				.getString(R.string.cloudsdale_user_endpoint);
		USERS_CLOUDS_ENDPOINT = appContext
				.getString(R.string.cloudsdale_user_clouds_endpoint);
	}

	private static AsyncHttpClient getAsyncClient() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.setUserAgent("cloudsdale-android");
		client.addHeader("Accept", "application/json");
		client.addHeader("Content-Encoding", "utf-8");
		client.addHeader("Content-Type", "application/json");
		return client;
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}

	private static void get(String relativeUrl,
			AsyncHttpResponseHandler responseHandler) {
		getAsyncClient().get(getAbsoluteUrl(relativeUrl), responseHandler);
	}

	private static void post(String relativeUrl, String json,
			AsyncHttpResponseHandler responseHandler) {
		try {
			getAsyncClient().post(Cloudsdale.getContext(),
					getAbsoluteUrl(relativeUrl),
					new StringEntity(json, "utf-8"), "application/json",
					responseHandler);
		} catch (UnsupportedEncodingException e) {
			// This won't happen
		}
	}

	public static void getSession(String email, String password,
			AsyncHttpResponseHandler handler) {
		try {
			String json = new JSONObject().put("email", email)
					.put("password", password).toString();
			post(SESSIONS_ENDPOINT, json, handler);
		} catch (JSONException e) {
			// This also won't happen
		}
	}

	public static void getSession(String oAuthId, Provider oAuthProvider,
			AsyncHttpResponseHandler handler) {
		// TODO Implement oAuth login
	}

	public static void getUser(String id, AsyncHttpResponseHandler handler) {
		String relUrl = String.format(USERS_ENDPOINT, id);
		get(relUrl, handler);
	}

	public static void getUserClouds(String id, AsyncHttpResponseHandler handler) {
		String relUrl = String.format(USERS_CLOUDS_ENDPOINT, id);
		get(relUrl, handler);
	}

}
