package org.cloudsdale.android.models.authentication;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;
import org.cloudsdale.android.models.LoggedUser;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.annotations.GsonIgnoreExclusionStrategy;
import org.cloudsdale.android.models.queries.SessionQuery;

import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Asynchronous authentication for Cloudsdale
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class CloudsdaleAsyncAuth extends
		AsyncTask<LoginBundle, String, LoggedUser> {

	public static final String	TAG	= "Cloudsdale AsyncAuth";
	protected Gson				gson;

	public String stripHtml(String html) {
		return Html.fromHtml(html).toString();
	}

	@Override
	protected LoggedUser doInBackground(LoginBundle... params) {
		// Create the post object
		QueryData data = new QueryData();
		data.setUrl(params[0].getLoginUrl());

		// Set email/password if supplied, else set the json
		if (params[0].getUsernameInput() != null
				&& params[0].getPasswordInput() != null) {
			ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(
					2);
			nameValuePairs.add(new BasicNameValuePair("email", params[0]
					.getUsernameInput()));
			nameValuePairs.add(new BasicNameValuePair("password", params[0]
					.getPasswordInput()));

			data.setHeaders(nameValuePairs);
		} else if (params[0].getoAuthBundle() != null) {
			// Build the json serializer
			GsonBuilder gb = new GsonBuilder();
			gb.serializeNulls();
			gb.setExclusionStrategies(new GsonIgnoreExclusionStrategy(
					String[].class));
			gson = gb.create();
			
			// Build the json to send off
			String oAuthJson = " { \"oauth\": "
					+ gson.toJson(params[0].getoAuthBundle(), OAuthBundle.class)
					+ "}";
			data.setJson(oAuthJson);
			Log.d(TAG, oAuthJson);
		}

		// Query the server and return the User to the caller
		SessionQuery query = new SessionQuery();
		LoggedUser u = query.execute(data);

		return u;
	}

}
