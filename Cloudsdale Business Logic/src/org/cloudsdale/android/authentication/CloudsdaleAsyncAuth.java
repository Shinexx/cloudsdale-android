package org.cloudsdale.android.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.cloudsdale.android.logic.PersistantData;
import org.cloudsdale.android.models.JsonResponse;
import org.cloudsdale.android.models.User;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

public class CloudsdaleAsyncAuth extends AsyncTask<LoginBundle, String, User> {

	@Override
	protected User doInBackground(LoginBundle... params) {
		try {
			// Create params for connection including 3sec timeout
			// on connection and 5sec timeout on socket
			HttpParams httpParams = new BasicHttpParams();
			int timeoutConnection = 3000;
			int timeoutSocket = 5000;

			// Set the timeouts
			HttpConnectionParams.setConnectionTimeout(httpParams,
					timeoutConnection);
			HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
			HttpClient httpClient = new DefaultHttpClient(httpParams);

			// Create the data entities
			HttpPost post = new HttpPost(params[0].getLoginUrl() + "sessions/");
			HttpResponse response;

			// Set POST data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("email", params[0]
					.getUsernameInput()));
			nameValuePairs.add(new BasicNameValuePair("password", params[0]
					.getPasswordInput()));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Query the server and store the user's ID
			response = httpClient.execute(post);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			// Get the user object
			Gson gson = new Gson();
			JsonResponse jsr = gson.fromJson(builder.toString(),
					JsonResponse.class);
			User u = jsr.getResult().getUser();

			return u;
		} catch (UnsupportedEncodingException e) {
			Log.e("Cloudsdale", e.getMessage());
		} catch (ClientProtocolException e) {
			Log.e("Cloudsdale", e.getMessage());
		} catch (IOException e) {
			Log.e("Cloudsdale", e.getMessage());
		}

		return null;
	}

	@Override
	protected void onPostExecute(User result) {
		super.onPostExecute(result);
		PersistantData.setMe(result);
	}
}
