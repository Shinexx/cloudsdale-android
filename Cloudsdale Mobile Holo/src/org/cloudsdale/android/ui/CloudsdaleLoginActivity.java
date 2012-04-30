package org.cloudsdale.android.ui;

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
import org.cloudsdale.android.models.JsonResponse;
import org.cloudsdale.android.models.User;
import org.cloudsdale.logic.UserJsonParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import com.google.gson.Gson;

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
	 * @see android.app.Activity#onCreate
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

	public void postAuth(User user) {
		if (user != null) {
			SharedPreferences.Editor edit = getPreferences(MODE_PRIVATE).edit();
			edit.putString("me", user.toString());
			edit.commit();
			
			Intent intent = new Intent();
			intent.setClass(CloudsdaleLoginActivity.this,
					MainViewActivity.class);
			startActivity(intent);
		}
		
		if (progressDialog != null) {
			progressDialog.cancel();
			progressDialog = null;
		}
	}

	public User readUserJson(JsonReader reader) {
		return new UserJsonParser().parseUserFromJson(reader);
	}

	public class CloudsdaleAsyncAuth extends AsyncTask<Void, String, User> {

		@Override
		protected User doInBackground(Void... params) {
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
						getString(R.string.cloudsdale_dev_api_url)
								+ "sessions/");
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
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));
				StringBuilder builder = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}

				// Get the user object
				publishProgress(new String[] { builder.toString() });

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
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);

			progressDialog.hide();
			new AlertDialog.Builder(CloudsdaleLoginActivity.this)
					.setTitle("JSON RESPONSE")
					.setMessage(values[0])
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
		}

		@Override
		protected void onPostExecute(User result) {
			super.onPostExecute(result);
			postAuth(result);
		}
	}
}