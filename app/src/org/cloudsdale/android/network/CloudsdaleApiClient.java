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

/**
 * An implemented API client for Cloudsdale.org Copyright(c) 2013 Cloudsdale.org
 * 
 * @author Jamison Greeley (berwyn.codeweaver@gmail.com)
 * 
 */
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

	/**
	 * Creates an API client, given the current application instance
	 * 
	 * @param cloudsdale
	 *            The current application instance
	 */
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
		hostUrl = service.get("host").getAsString();
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
		super.post(relUrl, json.toString(), "application/json", handler);
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
		super.post(relUrl, body.toString(), "application/json", handler);
	}

	/**
	 * Given a template to use, as well as an optional list of arguments,
	 * performs an HTTP GET on the resource <br/>
	 * The list used should be of the format {@code new List<String>()
	 * "replace this", "replace with this", "replace 2", "replace with 2"}
	 * 
	 * @param template
	 *            The template key to use
	 * @param arguments
	 *            The list of arguments to format the url stem with, alternating
	 *            between what key to use and wha to replace it with
	 * @param handler
	 *            Response handler to handle the failure or success of the
	 *            request
	 */
	public void get(String template, String[] arguments,
			AsyncHttpResponseHandler handler) {
		String urlStem = endpointTemplates.get(template);
		if (arguments != null && arguments.length > 0) {
			for (int i = 0; i < arguments.length; i++) {
				urlStem = urlStem.replace(arguments[i], arguments[i + 1]);
			}
		}
		super.get(urlStem, handler);
	}

	/**
	 * Given a template to use, as well as an optional list of arguments and a
	 * JSON body, performs an HTTP POST request on the resource <br/>
	 * The list used should be of the format {@code new List<String>()
	 * "replace this", "replace with this", "replace 2", "replace with 2"}
	 * 
	 * @param template
	 *            The template key to use
	 * @param arguments
	 *            The list of arguments to format the url stem with, alternating
	 *            between what key to use and wha to replace it with
	 * @param body
	 *            The JSON body to send to the server
	 * @param handler
	 *            Response handler to handle the failure or success of the
	 *            request
	 */
	public void post(String template, String[] arguments, JsonObject body,
			AsyncHttpResponseHandler handler) {
		String urlStem = endpointTemplates.get(template);
		if (arguments != null && arguments.length > 0) {
			for (int i = 0; i < arguments.length; i++) {
				urlStem = urlStem.replace(arguments[i], arguments[i + 1]);
			}
		}
		val bodyParsed = "";
		super.post(urlStem, bodyParsed, "application/json", handler);
	}

}
