package org.cloudsdale.android.ui;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.cloudsdale.android.models.User;
import org.cloudsdale.logic.UserJsonParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity used to login to Cloudsdale using the Cloudsdale API authentication
 * scheme.
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class CloudsdaleLoginActivity extends Activity {

	// Main GUI widgets
	private EditText		usernameField;
	private EditText		passwordField;
	private Button			loginButton;

	// Strings used in the authentication flow
	private String			usernameInput;
	private String			passwordInput;

	// Progress dialog to show while auth is occurring
	private ProgressDialog	progressDialog;

	/**
	 * @see android.app.Activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cloudsdale_login);

		// Pointing the data objects to the GUI widgets
		usernameField = (EditText) findViewById(R.id.cloudsdaleLoginUsernameField);
		passwordField = (EditText) findViewById(R.id.cloudsdaleLoginPasswordField);
		loginButton = (Button) findViewById(R.id.cloudsdaleLoginSubmitButton);

		// Cause the login button to execute the authentication flow
		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				usernameInput = usernameField.getText().toString();
				passwordInput = passwordField.getText().toString();

				doAuthentication();
			}
		});
	}

	/**
	 * Starts the authentication flow
	 */
	public void doAuthentication() {
		if (usernameInput != null && passwordInput != null) {
			progressDialog = ProgressDialog.show(this, "",
					"Logging in, one moment please", true);

			new CloudsdaleAsyncAuth().execute();
		}
	}

	public void postAuth(JsonReader reader) {
		User me = null;

		Log.d("Cloudsdale", reader.toString());
		
		try {
			reader.setLenient(true);
			reader.beginArray();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (name.equals("result")) {
					reader.beginObject();
					while (reader.hasNext()) {
						String _name = reader.nextName();
						if (_name.equals("user")) {
							me = readUserJson(reader);
						}
					}

					if (me != null) {
						SharedPreferences.Editor edit = getPreferences(
								MODE_PRIVATE).edit();
						edit.putString("me", me.toString());
						edit.commit();

						Intent intent = new Intent();
						intent.setClass(CloudsdaleLoginActivity.this,
								MainViewActivity.class);
						startActivity(intent);

					} else {
						Log.e("Cloudsdale", "user is null");
						Toast.makeText(this, "Couldn't log you in!",
								Toast.LENGTH_LONG);
					}
				}
			}

		} catch (IOException e) {
			Log.e("Cloudsdale", "Failure parsing JSONuser -- " + e.getMessage());
			Toast.makeText(this, "Couldn't log you in!", Toast.LENGTH_LONG);
		} catch (IllegalStateException e) {
			Log.e("Cloudsdale", "Failure parsing JSONuser -- " + e.getMessage());
			Toast.makeText(this, "Couldn't log you in!", Toast.LENGTH_LONG);
		}

		if (progressDialog != null) {
			progressDialog.cancel();
			progressDialog = null;
		}
	}

	public User readUserJson(JsonReader reader) {
		return new UserJsonParser().parseUserFromJson(reader);
	}

	public class CloudsdaleAsyncAuth extends
			AsyncTask<Void, String, JsonReader> {

		@Override
		protected JsonReader doInBackground(Void... params) {
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
				HttpPost post = new HttpPost(
						getString(R.string.cloudsdale_dev_api_url));
				HttpResponse response;

				// Set POST data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("email",
						usernameInput));
				nameValuePairs.add(new BasicNameValuePair("password",
						passwordInput));
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Query the server and store the user's ID
				response = httpClient.execute(post);
				JsonReader reader = new JsonReader(new InputStreamReader(response
						.getEntity().getContent(), "UTF-8"));
				return reader;
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
		protected void onPostExecute(JsonReader result) {
			super.onPostExecute(result);
			postAuth(result);
		}
	}
}