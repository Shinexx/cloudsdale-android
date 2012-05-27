package org.cloudsdale.android.authentication;

import java.net.NetPermission;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.cloudsdale.android.annotations.GsonIgnoreExclusionStrategy;
import org.cloudsdale.android.logic.PersistentData;
import org.cloudsdale.android.logic.PostQueryObject;
import org.cloudsdale.android.models.LoginResponse;
import org.cloudsdale.android.models.User;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Asynchronous authentication for Cloudsdale
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class CloudsdaleAsyncAuth extends AsyncTask<LoginBundle, String, User> {

	public static final String	TAG	= "Cloudsdale AsyncAuth";
	protected ProgressDialog	dialog;
	protected Gson				gson;

	public String stripHtml(String html) {
		return Html.fromHtml(html).toString();
	}

	@Override
	protected User doInBackground(LoginBundle... params) {
		// Build the json serializer
		GsonBuilder gb = new GsonBuilder();
		gb.serializeNulls();
		gb.setExclusionStrategies(new GsonIgnoreExclusionStrategy(
				String[].class));
		Gson gson = gb.create();

		PostQueryObject post = null;

		// Set POST data
		// Set entity data
		if (params[0].getUsernameInput() != null
				&& params[0].getPasswordInput() != null) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("email", params[0]
					.getUsernameInput()));
			nameValuePairs.add(new BasicNameValuePair("password", params[0]
					.getPasswordInput()));

			post = new PostQueryObject(nameValuePairs, params[0].getLoginUrl());
		} else if (params[0].getoAuthBundle() != null) {
			String oAuthJson = " { \"oauth\": "
					+ gson.toJson(params[0].getoAuthBundle(), OAuthBundle.class)
					+ "}";
			post = new PostQueryObject(oAuthJson, params[0].getLoginUrl(),
					params[0].getAuthToken());
			Log.d(TAG, oAuthJson);
		}

		// Query the server
		String response = post.execute();

		// Get the user object
		try {
			Log.d(TAG, response);
			LoginResponse jsonResponse = gson.fromJson(stripHtml(response),
					LoginResponse.class);
			if (jsonResponse != null) {
				if (jsonResponse.getErrors() == null) {
					User u = jsonResponse.getResult().getUser();
					Log.d(TAG,
							"User received: " + u.getEmail() + " "
									+ u.getName());
					return u;
				} else {
					Log.e(TAG, "There was an error");
					cancel(true);
					// TODO Handle user parse error
					return null;
				}
			} else {
				Log.e(TAG, "No user received");
				cancel(true);
				// TODO Handle not getting a user
				return null;
			}
		} catch (JsonSyntaxException e) {
			cancel(true);
			Log.e(TAG, "Json Syntax Exception: " + e.getMessage());
		} catch (NullPointerException e) {
			cancel(true);
			Log.e(TAG, "Null pointer: " + e.getMessage());
		} catch (IllegalStateException e) {
			cancel(true);
			Log.e(TAG, "Illegal state: " + e.getMessage());
		} catch (Exception e) {
			cancel(true);
		}

		return null;
	}

	@Override
	protected void onPostExecute(User result) {
		super.onPostExecute(result);
		PersistentData.setMe(result);
		if (dialog != null) {
			dialog.dismiss();
		}
	}
}
