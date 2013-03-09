package org.cloudsdale.android.network;

import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.cloudsdale.android.BCrypt;
import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.network.Provider;

import java.util.HashMap;
import java.util.Map;

import lombok.val;

public class CloudsdaleApiClient extends AbstractApiClient {

	private Cloudsdale							mAppInstance;
	@SuppressWarnings("serial")
	private static final Map<String, String>	headers	= new HashMap<String, String>() {
															{
																put("Accept",
																		"application/json");
																put("Content-Encoding",
																		"utf-8");
																put("Content-Type",
																		"application/json");
															}
														};

	public CloudsdaleApiClient(Cloudsdale cloudsdale) {
		super("cloudsdale-android", headers);
		mAppInstance = cloudsdale;
		addHeader("X_AUTH_INTERNAL_TOKEN",
				mAppInstance.getString(R.string.cloudsdale_auth_token));
		setContext(mAppInstance.getContext());
	}

	/**
	 * Given a list of services, configures itself to work for Cloudsdale
	 * 
	 * @param services
	 *            The list of services this app supports
	 */
	public void configure(JsonObject service) {
		mHostUrl = service.get("host").getAsString();
		processEndpoints(service.getAsJsonArray("endpoints"));
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
	 */
	public void postSession(String email, String password,
			AsyncHttpResponseHandler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("email", email);
		json.addProperty("password", password);
		val relUrl = buildRelUrl("sessions");
		post(relUrl, json.toString(), handler);
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
	 */
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

	/**
	 * User Resources
	 */

	/**
	 * TODO - document this?
	 * 
	 * @param handler
	 */
	public void getUsers(AsyncHttpResponseHandler handler) {
		val relUrl = buildRelUrl("users");
		get(relUrl, handler);
	}

	/**
	 * Given a user's ID, fetches their metadata
	 * 
	 * @param id
	 *            The user's ID
	 * @param handler
	 *            Response handler to handle the failure or success of the
	 *            request
	 */
	public void getUser(String id, AsyncHttpResponseHandler handler) {
		String relUrl = buildRelUrl("user", "{userid}", id);
		get(relUrl, handler);
	}

	/**
	 * 
	 * @param handler
	 */
	public void getUserRestore(AsyncHttpResponseHandler handler) {
		val relUrl = buildRelUrl("users:restore");
		get(relUrl, handler);
	}

	/**
	 * Given a user's ID, gets all the clouds that user belongs to
	 * 
	 * @param id
	 *            The user's ID
	 * @param handler
	 *            Response handler to handle the failure or success of the
	 *            request
	 */
	public void getUserClouds(String id, AsyncHttpResponseHandler handler) {
		String relUrl = buildRelUrl("user:clouds", "{userid}", id);
		get(relUrl, handler);
	}

	/**
	 * 
	 * Cloud Resources
	 * 
	 */

	/**
	 * Gets a cloud by ID
	 * 
	 * @param id
	 *            The cloud's ID
	 * @param handler
	 *            Response handler to handle the failure or success of the
	 *            request
	 */
	public void getCloud(String id, AsyncHttpResponseHandler handler) {
		String relUrl = buildRelUrl("cloud", "{cloudid}", id);
		get(relUrl, handler);
	}

	/**
	 * Get the bans associated with a given cloud
	 * 
	 * @param id
	 *            The cloud's ID
	 * @param handler
	 *            Response handler to handle the failure or success of the
	 *            request
	 */
	public void getCloudBans(String id, AsyncHttpResponseHandler handler) {
		String relUrl = buildRelUrl("cloud:bans", "{cloudid}", id);
		get(relUrl, handler);
	}

	/**
	 * Get the drops associated with a given cloud
	 * 
	 * @param id
	 *            The cloud's ID
	 * @param handler
	 *            Response handler to handle the failure or success of the
	 *            request
	 */
	public void getCloudDrops(String id, AsyncHttpResponseHandler handler) {
		String relUrl = buildRelUrl("cloud:drops", "{cloudid}", id);
		get(relUrl, handler);
	}

	/**
	 * Get the last fifty messages associated with a given cloud
	 * 
	 * @param id
	 *            The cloud's ID
	 * @param handler
	 *            Response handler to handle the failure or success of the
	 *            request
	 */
	public void getCloudMessages(String id, AsyncHttpResponseHandler handler) {
		String relUrl = buildRelUrl("cloud:messages", "{cloudid}", id);
		get(relUrl, handler);
	}

	/**
	 * Gets the most popular clouds currently on Cloudsdale
	 * 
	 * @param handler
	 *            Response handler to handle the failure or success of the
	 *            request
	 */
	public void getCloudPopular(AsyncHttpResponseHandler handler) {
		val relUrl = buildRelUrl("clouds:popular");
		get(relUrl, handler);
	}

	/**
	 * Gets the most recently created clouds on Cloudsdale
	 * 
	 * @param handler
	 *            Response handler to handle the failure or success of the
	 *            request
	 */
	public void getCloudRecents(AsyncHttpResponseHandler handler) {
		val relUrl = buildRelUrl("clouds:recent");
		get(relUrl, handler);
	}

	/**
	 * Searches for clouds based on the given query
	 * 
	 * @param query
	 *            String query to find clouds based upon
	 * @param handler
	 *            Respones handler to handle the failure or success of the
	 *            request
	 */
	public void postCloudSearch(String query, AsyncHttpResponseHandler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("q", query);
		val relUrl = buildRelUrl("clouds:search");
		post(relUrl, json.toString(), handler);
	}
}
