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

	private Cloudsdale	mAppInstance;
	private String		mBaseUrl;
	private String		mSessionEndpoint;
	private String		mUserEndpoint;
	private String		mUserCloudEndpoint;

	public CloudsdaleApiClient(Cloudsdale cloudsdale) {
		mAppInstance = cloudsdale;
		mBaseUrl = mAppInstance.getString(R.string.cloudsdale_api_base);
		mSessionEndpoint = mAppInstance
				.getString(R.string.cloudsdale_sessions_endpoint);
		mUserEndpoint = mAppInstance
				.getString(R.string.cloudsdale_user_endpoint);
		mUserCloudEndpoint = mAppInstance
				.getString(R.string.cloudsdale_user_clouds_endpoint);
	}

	private AsyncHttpClient getAsyncClient() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.setUserAgent("cloudsdale-android");
		client.addHeader("Accept", "application/json");
		client.addHeader("Content-Encoding", "utf-8");
		client.addHeader("Content-Type", "application/json");
		return client;
	}

	private String getAbsoluteUrl(String relativeUrl) {
		return mBaseUrl + relativeUrl;
	}

	private void get(String relativeUrl,
			AsyncHttpResponseHandler responseHandler) {
		getAsyncClient().get(getAbsoluteUrl(relativeUrl), responseHandler);
	}

	private void post(String relativeUrl, String json,
			AsyncHttpResponseHandler responseHandler) {
		try {
			getAsyncClient().post(mAppInstance.getContext(),
					getAbsoluteUrl(relativeUrl),
					new StringEntity(json, "utf-8"), "application/json",
					responseHandler);
		} catch (UnsupportedEncodingException e) {
			// This won't happen
		}
	}

	public void getSession(String email, String password,
			AsyncHttpResponseHandler handler) {
		try {
			String json = new JSONObject().put("email", email)
					.put("password", password).toString();
			post(mSessionEndpoint, json, handler);
		} catch (JSONException e) {
			// This also won't happen
		}
	}

	public void getSession(String oAuthId, Provider oAuthProvider,
			AsyncHttpResponseHandler handler) {
		// TODO Implement oAuth login
	}

	public void getUser(String id, AsyncHttpResponseHandler handler) {
		String relUrl = String.format(mUserEndpoint, id);
		get(relUrl, handler);
	}

	public void getUserClouds(String id, AsyncHttpResponseHandler handler) {
		String relUrl = String.format(mUserCloudEndpoint, id);
		get(relUrl, handler);
	}

}
