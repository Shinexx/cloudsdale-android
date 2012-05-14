package org.cloudsdale.android.authentication;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.cloudsdale.android.R;
import org.cloudsdale.android.logic.PersistentData;
import org.cloudsdale.android.logic.PostQueryObject;
import org.cloudsdale.android.models.Response;
import org.cloudsdale.android.models.User;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Asynchronous authentication for Cloudsdale
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class CloudsdaleAsyncAuth extends AsyncTask<LoginBundle, String, User> {

	public static final String	TAG	= "Cloudsdale AsyncAuth";

	@Override
	protected User doInBackground(LoginBundle... params) {
		// Set POST data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("email", params[0]
				.getUsernameInput()));
		nameValuePairs.add(new BasicNameValuePair("password", params[0]
				.getPasswordInput()));
		nameValuePairs.add(new BasicNameValuePair("X_AUTH_INTERNAL_TOKEN",
				params[0].getAuthToken()));

		// Create the post object
		PostQueryObject post = new PostQueryObject(nameValuePairs,
				params[0].getLoginUrl());

		// Query the server
		String response = post.execute();

		// Get the user object
		Gson gson = new Gson();
		Response jsonResponse = gson.fromJson(response, Response.class);
		if (jsonResponse != null) {
			if (jsonResponse.getError() == null) {
				User u = gson.fromJson(jsonResponse.getResult(), User.class);
				Log.e(TAG, "User received");
				return u;
			} else {
				return new User();
			}
		} else {
			Log.e(TAG, "No user received");
			return new User();
		}
	}

	@Override
	protected void onPostExecute(User result) {
		super.onPostExecute(result);
		PersistentData.setMe(result);
	}
}
