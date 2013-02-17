package org.cloudsdale.android.network;

import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;
import org.cloudsdale.android.BCrypt;
import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.network.Provider;

import java.io.UnsupportedEncodingException;

public class CloudsdaleApiClient {

	private Cloudsdale		mAppInstance;
	private String			mBaseUrl;
	private String			mSessionEndpoint;
	private String			mUserEndpoint;
	private String			mUserCloudEndpoint;
	private String			mCloudEndpoint;
	private String			mCloudBansEndpoint;
	private String			mCloudDropsEndpoint;
	private String			mCloudMessagesEndpoint;
	private String			mCloudPopularEndpoint;
	private String			mCloudRecentEndpoint;
	private String			mCloudSearchEndpoint;
	private AsyncHttpClient	mClient;

	public CloudsdaleApiClient(Cloudsdale cloudsdale) {
		mAppInstance = cloudsdale;
		mBaseUrl = mAppInstance.getString(R.string.cloudsdale_api_base);
		mSessionEndpoint = mAppInstance
				.getString(R.string.cloudsdale_sessions_endpoint);
		mUserEndpoint = mAppInstance
				.getString(R.string.cloudsdale_user_endpoint);
		mUserCloudEndpoint = mAppInstance
				.getString(R.string.cloudsdale_user_clouds_endpoint);
		mCloudEndpoint = mAppInstance
				.getString(R.string.cloudsdale_cloud_endpoint);
		mCloudBansEndpoint = mAppInstance
				.getString(R.string.cloudsdale_cloud_bans_endpoint);
		mCloudDropsEndpoint = mAppInstance
				.getString(R.string.cloudsdale_cloud_drop_endpoint);
		mCloudMessagesEndpoint = mAppInstance
				.getString(R.string.cloudsdale_cloud_chat_messages_endpoint);
		mCloudPopularEndpoint = mAppInstance
				.getString(R.string.cloudsdale_cloud_popular_endpoint);
		mCloudRecentEndpoint = mAppInstance
				.getString(R.string.cloudsdale_cloud_recent_endpoint);
		mCloudSearchEndpoint = mAppInstance
				.getString(R.string.cloudsdale_cloud_search_endpoint);
	}

	private AsyncHttpClient getAsyncClient() {
		if (mClient == null) {
			mClient = new AsyncHttpClient();
			mClient.setUserAgent("cloudsdale-android");
			mClient.addHeader("Accept", "application/json");
			mClient.addHeader("Content-Encoding", "utf-8");
			mClient.addHeader("Content-Type", "application/json");
			mClient.addHeader("X_AUTH_INTERNAL_TOKEN",
					mAppInstance.getString(R.string.cloudsdale_auth_token));
		}
		return mClient;
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
			String url = getAbsoluteUrl(relativeUrl);
			getAsyncClient().post(mAppInstance.getContext(), url,
					new StringEntity(json, "utf-8"), "application/json",
					responseHandler);
		} catch (UnsupportedEncodingException e) {
			// This won't happen
		}
	}

	public void getCloud(String id, AsyncHttpResponseHandler handler) {
		String relUrl = String.format(mCloudEndpoint, id);
		get(relUrl, handler);
	}

	public void getCloudBans(String id, AsyncHttpResponseHandler handler) {
		String relUrl = String.format(mCloudBansEndpoint, id);
		get(relUrl, handler);
	}

	public void getCloudDrops(String id, AsyncHttpResponseHandler handler) {
		String relUrl = String.format(mCloudDropsEndpoint, id);
		get(relUrl, handler);
	}

	public void getCloudMessages(String id, AsyncHttpResponseHandler handler) {
		String relUrl = String.format(mCloudMessagesEndpoint, id);
		get(relUrl, handler);
	}

	public void getCloudPopular(AsyncHttpResponseHandler handler) {
		get(mCloudPopularEndpoint, handler);
	}

	public void getCloudRecents(AsyncHttpResponseHandler handler) {
		get(mCloudRecentEndpoint, handler);
	}

	public void postCloudSearch(String query, AsyncHttpResponseHandler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("q", query);
		post(mCloudSearchEndpoint, json.toString(), handler);
	}

	public void getUser(String id, AsyncHttpResponseHandler handler) {
		String relUrl = String.format(mUserEndpoint, id);
		get(relUrl, handler);
	}

	public void getUserClouds(String id, AsyncHttpResponseHandler handler) {
		String relUrl = String.format(mUserCloudEndpoint, id);
		get(relUrl, handler);
	}

	public void postSession(String email, String password,
			AsyncHttpResponseHandler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("email", email);
		json.addProperty("password", password);
		post(mSessionEndpoint, json.toString(), handler);
	}

	public void postSession(String oAuthId, Provider oAuthProvider,
			String internalToken, AsyncHttpResponseHandler handler) {
		JsonObject oAuth = new JsonObject();
		oAuth.addProperty("cli_type", "android");
		oAuth.addProperty("provider", oAuthProvider.toString());
		oAuth.addProperty("uid", oAuthId);
		oAuth.addProperty("token", BCrypt.hashpw(
				oAuthId + oAuthProvider.toString(), internalToken));
		JsonObject body = new JsonObject();
		body.add("oauth", oAuth);
		post(mSessionEndpoint, body.toString(), handler);
	}

}
