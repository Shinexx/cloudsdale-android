package org.cloudsdale.android.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;
import org.cloudsdale.android.BCrypt;
import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.network.Provider;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import lombok.val;

public class CloudsdaleApiClient {

	private Cloudsdale			mAppInstance;
	private AsyncHttpClient		mClient;
	private String				mHostUrl;
	private Map<String, String>	mEndpointTemplates;

	public CloudsdaleApiClient(Cloudsdale cloudsdale) {
		mAppInstance = cloudsdale;
		mEndpointTemplates = new HashMap<String, String>();
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

	private String getBaseUrl() {
		if (mHostUrl == null) {
			JsonArray array = mAppInstance.getConfig().getAsJsonArray(
					"services");
			for (JsonElement element : array) {
				JsonObject obj = element.getAsJsonObject();
				if (obj.get("id").equals("cloudsdale")) { return obj
						.get("host").getAsString(); }
			}
			return null;
		} else {
			return mHostUrl;
		}
	}

	private String getAbsoluteUrl(String relativeUrl) {
		return getBaseUrl() + relativeUrl;
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

	private String buildRelUrl(String template) {
		return mEndpointTemplates.get(template);
	}

	private String buildRelUrl(String template, String replace,
			String replaceArg) {
		return buildRelUrl(template).replace(replace, replaceArg);
	}

	private String buildRelUrl(String template, String replace,
			String replaceArg, String subRes, String subResReplace) {
		return buildRelUrl(template, replace, replaceArg).replace(subRes,
				subResReplace);
	}

	public void processEndpoints(JsonArray endpoints) {
		for (JsonElement element : endpoints) {
			val obj = element.getAsJsonObject();
			mEndpointTemplates.put(obj.get("id").toString(), obj
					.get("template").toString());
		}
	}

	public void getCloud(String id, AsyncHttpResponseHandler handler) {
		String relUrl = buildRelUrl("cloud", "{cloudid}", id);
		get(relUrl, handler);
	}

	public void getCloudBans(String id, AsyncHttpResponseHandler handler) {
		String relUrl = buildRelUrl("cloud:bans", "{cloudid}", id);
		get(relUrl, handler);
	}

	public void getCloudDrops(String id, AsyncHttpResponseHandler handler) {
		String relUrl = buildRelUrl("cloud:drops", "{cloudid}", id);
		get(relUrl, handler);
	}

	public void getCloudMessages(String id, AsyncHttpResponseHandler handler) {
		String relUrl = buildRelUrl("cloud:messages", "{cloudid}", id);
		get(relUrl, handler);
	}

	public void getCloudPopular(AsyncHttpResponseHandler handler) {
		val relUrl = buildRelUrl("clouds:popular");
		get(relUrl, handler);
	}

	public void getCloudRecents(AsyncHttpResponseHandler handler) {
		val relUrl = buildRelUrl("clouds:recent");
		get(relUrl, handler);
	}

	public void postCloudSearch(String query, AsyncHttpResponseHandler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("q", query);
		val relUrl = buildRelUrl("clouds:search");
		post(relUrl, json.toString(), handler);
	}

	public void getUser(String id, AsyncHttpResponseHandler handler) {
		String relUrl = buildRelUrl("user", "{userid}", id);
		get(relUrl, handler);
	}

	public void getUserClouds(String id, AsyncHttpResponseHandler handler) {
		String relUrl = buildRelUrl("user:clouds", "{userid}", id);
		get(relUrl, handler);
	}

	public void postSession(String email, String password,
			AsyncHttpResponseHandler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("email", email);
		json.addProperty("password", password);
		val relUrl = buildRelUrl("sessions");
		post(relUrl, json.toString(), handler);
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
		val relUrl = buildRelUrl("sessions");
		post(relUrl, body.toString(), handler);
	}

}
