package org.cloudsdale.android.authentication;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.cloudsdale.android.logic.PersistentData;
import org.cloudsdale.android.logic.PostQueryObject;
import org.cloudsdale.android.models.Response;
import org.cloudsdale.android.models.User;

import android.os.AsyncTask;

import com.google.gson.Gson;

/**
 * Asynchronous authentication for Cloudsdale
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class CloudsdaleAsyncAuth extends AsyncTask<LoginBundle, String, User> {

	@Override
	protected User doInBackground(LoginBundle... params) {
		// Set POST data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("email", params[0]
				.getUsernameInput()));
		nameValuePairs.add(new BasicNameValuePair("password", params[0]
				.getPasswordInput()));

		// Create the post object
		PostQueryObject post = new PostQueryObject(nameValuePairs,
				params[0].getLoginUrl());

		// Query the server and store the user's ID
		String response = post.execute();

		// Get the user object
		Gson gson = new Gson();
		Response jsonResponse = gson.fromJson(response, Response.class);
		User u = gson.fromJson(jsonResponse.getResult(), User.class);

		return u;
	}

	@Override
	protected void onPostExecute(User result) {
		super.onPostExecute(result);
		PersistentData.setMe(result);
	}
}
