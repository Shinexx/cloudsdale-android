package org.cloudsdale.android.network;

import android.util.Log;

import com.koushikdutta.async.http.AsyncHttpClient;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.network.Provider;
import org.cloudsdale.android.util.BCrypt;
import org.codeweaver.remoteconfiguredhttpclient.AbstractApiClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * An implemented API client for Cloudsdale.org Copyright(c) 2013 Cloudsdale.org
 * 
 * @author Jamison Greeley (berwyn.codeweaver@gmail.com)
 * 
 */
public class CloudsdaleApiClient extends AbstractApiClient {

	private static final String					TAG					= "Cloudsdale API Client";

	private Cloudsdale							cloudsdale;

	@SuppressWarnings("serial")
	private static final Map<String, String>	cloudsdaleHeaders	= new HashMap<String, String>() {
																		{
																			put("Accept",
																					"application/json");
																			put("Content-Encoding",
																					"utf-8");
																			put("Content-Type",
																					"application/json");
																		}
																	};

	/**
	 * Creates an API client, given the current application instance
	 * 
	 * @param cloudsdale
	 *            The current application instance
	 */
	public CloudsdaleApiClient(Cloudsdale cloudsdale) {
		super("cloudsdale-android", cloudsdaleHeaders);
		this.cloudsdale = cloudsdale;
	}

	/**
	 * Given a service, configures itself to work for Cloudsdale
	 * 
	 * @param service
	 *            The JsonObject representing this service
	 */
	public void configure(JSONObject service) throws JSONException {
		addHeader("X_AUTH_INTERNAL_TOKEN",
				cloudsdale.getString(R.string.cloudsdale_auth_token));
		if(cloudsdale.isDebuggable()) {
			Log.d(TAG, "Configuring with \n" + service.toString());
			Log.d(TAG, "Configuring with host string [" + service.getString("host") + "]");
		}
		setHostUrl(service.getString("host"));
		configureEndpoints(service.getJSONArray("endpoints"), "id", "template");
	}

	/**
	 * Session Resources
	 */

	/**
	 * Given an email address and password, establishes a session
	 * 
	 * @param email
	 *            The user's email address
	 * @param password
	 *            The user's password
	 * @param handler
	 *            Response handler to handle the failure or success of the
	 *            request
	 * @throws JSONException
	 */
	public void postSession(String email, String password,
			AsyncHttpClient.JSONObjectCallback callback) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("email", email);
		json.put("password", password);
		post("sessions", null, json, callback);
	}

	/**
	 * Given oAuth credentials, establishes a session with the server
	 * 
	 * @param oAuthId
	 *            The ID provided by the oAuth service
	 * @param oAuthProvider
	 *            Provider object stating where the credentials come from
	 * @param internalToken
	 *            The internal token verifying that this client may access
	 *            private API functions
	 * @param handler
	 *            Response handler to handle the failure or success of the
	 *            request
	 * @throws JSONException
	 */
	public void postSession(String oAuthId, Provider oAuthProvider,
			String internalToken, AsyncHttpClient.JSONObjectCallback callback)
			throws JSONException {
		JSONObject oAuth = new JSONObject();
		oAuth.put("cli_type", "android");
		oAuth.put("provider", oAuthProvider.toString());
		oAuth.put("uid", oAuthId);
		oAuth.put("token", BCrypt.hashpw(oAuthId + oAuthProvider.toString(),
				internalToken));
		JSONObject body = new JSONObject();
		body.put("oauth", oAuth);
		if (cloudsdale.isDebuggable()) {
			Log.d(TAG, buildUrl("sessions", null));
		}
		post("sessions", null, body, callback);
	}

}
