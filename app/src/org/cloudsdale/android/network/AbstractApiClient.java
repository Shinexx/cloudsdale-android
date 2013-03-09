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

/**
 * Common code for creating RESTful API clients based on an asynchronus HTTP
 * client Copyright (c) 2013 Cloudsdale.org
 * 
 * @author Jamison Greeley (berwyn.codeweaver@gmail.com)
 * 
 */
abstract class AbstractApiClient {

	protected AsyncHttpClient		httpClient;
	protected String				hostUrl;
	protected Map<String, String>	endpointTemplates;
	private String					userAgent;
	private Map<String, String>		headers;
	private Context					context;

	/**
	 * Basic constructor for API clients
	 * 
	 * @param userAgent
	 *            The useragent to use in HTTP transactions
	 * @param headers
	 *            Any headers to be sent with HTTP requests
	 */
	protected AbstractApiClient(String userAgent, Map<String, String> headers) {
		endpointTemplates = new HashMap<String, String>();
		this.userAgent = userAgent;
		this.headers = headers;
	}

	/**
	 * Given a JsonArray of endpoints, indexes the templates so they can be
	 * looked up via their IDs
	 * 
	 * @param endpoints
	 *            A JsonArray of endpoints
	 */
	protected void processEndpoints(JsonArray endpoints) {
		for (JsonElement element : endpoints) {
			val obj = element.getAsJsonObject();
			endpointTemplates.put(obj.get("id").toString(), obj.get("template")
					.toString());
		}
	}

	/**
	 * Sets the context required by POST requests
	 * 
	 * @param context
	 *            The context to be used for HTTP transactions
	 */
	protected void setContext(Context context) {
		this.context = context;
	}

	/**
	 * Adds a header to the list of headers on the HTTP client
	 * 
	 * @param key
	 *            The HTTP header key
	 * @param value
	 *            The value associated with this header
	 */
	protected void addHeader(String key, String value) {
		headers.put(key, value);
	}

	/**
	 * Performs an HTTP GET request on the given URL
	 * 
	 * @param url
	 *            The URL fragment to perform the request on
	 * @param responseHandler
	 *            Response handler that handles the failure or success of the
	 *            query
	 */
	protected void get(String url, AsyncHttpResponseHandler responseHandler) {
		val fullUrl = getAbsoluteUrl(url);
		getAsyncClient().get(fullUrl, responseHandler);
	}

	/**
	 * Given a URL and JSON body, executes an HTTP POST request
	 * 
	 * @param url
	 *            The URL fragment to perform the request on
	 * @param body
	 *            The UTF-8 encoded text body to send to the server
	 * @param mimeType
	 * 			  The mimetype of the text body being sent           
	 * @param responseHandler
	 *            Response handler that handles the failure or success of the
	 *            query
	 */
	protected void post(String url, String body, String mimeType,
			AsyncHttpResponseHandler responseHandler) {
		try {
			val fullUrl = getAbsoluteUrl(url);
			getAsyncClient().post(context, fullUrl,
					new StringEntity(body, "utf-8"), mimeType, responseHandler);
		} catch (UnsupportedEncodingException e) {
			// This won't happen
		}
	}

	/**
	 * Given a url template id, builds the full URL stem
	 * 
	 * @param template
	 *            The string ID of the template to use
	 * @return The full URL stem
	 */
	protected String buildRelUrl(String template) {
		return endpointTemplates.get(template);
	}

	/**
	 * Given a template and formatting arguments, builds the full URL stem with
	 * the formatted data
	 * 
	 * @param template
	 *            The string ID of the template to use
	 * @param replace
	 *            The placeholder text to replace
	 * @param replaceArg
	 *            The value to replace with
	 * @return The full URL stem
	 */
	protected String buildRelUrl(String template, String replace,
			String replaceArg) {
		return buildRelUrl(template).replace(replace, replaceArg);
	}

	/**
	 * Given a template and formatting arguments, builds the full URL stem for a
	 * nested resource with the formatted data
	 * 
	 * @param template
	 *            The string ID of the template to use
	 * @param replace
	 *            The placeholder text to replace
	 * @param replaceArg
	 *            The value to replace with
	 * @param subRes
	 *            The nested resource's placeholder text to replace
	 * @param subResReplace
	 *            the value to replace for the nested resource
	 * @return The full URL stem
	 */
	protected String buildRelUrl(String template, String replace,
			String replaceArg, String subRes, String subResReplace) {
		return buildRelUrl(template, replace, replaceArg).replace(subRes,
				subResReplace);
	}

	/**
	 * Gets the async client, building as needed
	 * 
	 * @return The current async client
	 */
	private AsyncHttpClient getAsyncClient() {
		if (httpClient == null) {
			httpClient = new AsyncHttpClient();
			httpClient.setUserAgent(userAgent);
			for (Entry<String, String> header : headers.entrySet()) {
				httpClient.addHeader(header.getKey(), header.getValue());
			}
		}
		return httpClient;
	}

	/**
	 * Builds an absolute URL based on a URL stem
	 * 
	 * @param relativeUrl
	 *            The URL stem to expand
	 * @return The full URL
	 */
	private String getAbsoluteUrl(String relativeUrl) {
		return hostUrl + relativeUrl;
	}
}
