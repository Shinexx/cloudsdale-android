package org.cloudsdale.android.network;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import lombok.val;

abstract class AbstractApiClient {

	protected AsyncHttpClient		mClient;
	protected String				mHostUrl;
	protected Map<String, String>	mEndpointTemplates;
	private String					userAgent;
	private Map<String, String>		headers;
	private Context					context;

	protected AbstractApiClient(String userAgent, Map<String, String> headers) {
		mEndpointTemplates = new HashMap<String, String>();
		this.userAgent = userAgent;
		this.headers = headers;
	}

	protected void processEndpoints(JsonArray endpoints) {
		for (JsonElement element : endpoints) {
			val obj = element.getAsJsonObject();
			mEndpointTemplates.put(obj.get("id").toString(), obj
					.get("template").toString());
		}
	}
	
	protected void setContext(Context context) {
		this.context = context;
	}

	protected void addHeader(String key, String value) {
		headers.put(key, value);
	}
	
	protected void get(String url, AsyncHttpResponseHandler responseHandler) {
		getAsyncClient().get(url, responseHandler);
	}

	protected void post(String relativeUrl, String json,
			AsyncHttpResponseHandler responseHandler) {
		try {
			String url = getAbsoluteUrl(relativeUrl);
			getAsyncClient().post(context, url,
					new StringEntity(json, "utf-8"), "application/json",
					responseHandler);
		} catch (UnsupportedEncodingException e) {
			// This won't happen
		}
	}

	protected String buildRelUrl(String template) {
		return mEndpointTemplates.get(template);
	}

	protected String buildRelUrl(String template, String replace,
			String replaceArg) {
		return buildRelUrl(template).replace(replace, replaceArg);
	}

	protected String buildRelUrl(String template, String replace,
			String replaceArg, String subRes, String subResReplace) {
		return buildRelUrl(template, replace, replaceArg).replace(subRes,
				subResReplace);
	}
	
	private AsyncHttpClient getAsyncClient() {
		if (mClient == null) {
			mClient = new AsyncHttpClient();
			mClient.setUserAgent(userAgent);
			for (Entry<String, String> header : headers.entrySet()) {
				mClient.addHeader(header.getKey(), header.getValue());
			}
		}
		return mClient;
	}

	private String getAbsoluteUrl(String relativeUrl) {
		return mHostUrl + relativeUrl;
	}
}
