package org.cloudsdale.android.authentication;

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
import android.util.Log;

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

	@Override
	protected User doInBackground(LoginBundle... params) {
		// Set POST data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		// Add required info
		nameValuePairs.add(new BasicNameValuePair("X_AUTH_INTERNAL_TOKEN",
				params[0].getAuthToken()));
		// Check for passed parameters
		// usually email/password and oAuth are mutually exclusive
		if (params[0].getUsernameInput() != null) {
			nameValuePairs.add(new BasicNameValuePair("email", params[0]
					.getUsernameInput()));
		}
		if (params[0].getPasswordInput() != null) {
			nameValuePairs.add(new BasicNameValuePair("password", params[0]
					.getPasswordInput()));
		}
		if (params[0].getoAuthBundle() != null) {
			nameValuePairs.add(new BasicNameValuePair("oAuth", params[0]
					.getoAuthBundle().toString()));
			Log.d(TAG, params[0].getoAuthBundle().toString());
		}

		// Create the post object
		PostQueryObject post = new PostQueryObject(nameValuePairs,
				params[0].getLoginUrl());

		// Query the server
		String response = post.execute();

		// Get the user object
		GsonBuilder gb = new GsonBuilder();
		gb.serializeNulls();
		gb.setExclusionStrategies(new GsonIgnoreExclusionStrategy(
				String[].class));
		Gson gson = gb.create();
		try {
			Log.d(TAG, response);
			LoginResponse jsonResponse = gson.fromJson(response,
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
					return null;
				}
			} else {
				Log.e(TAG, "No user received");
				return null;
			}
		} catch (JsonSyntaxException e) {
			Log.e(TAG, "Json Syntax Exception: " + e.getMessage());
			return null;
		} catch (NullPointerException e) {
			Log.e(TAG, "Null pointer: " + e.getMessage());
			return null;
		}
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
