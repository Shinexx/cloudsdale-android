package org.cloudsdale.android.ui;

import android.accounts.Account;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.SherlockActivity;
import com.bugsense.trace.BugSenseHandler;
import com.google.gson.JsonObject;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;

import java.io.IOException;

import lombok.val;

public class StartActivity extends SherlockActivity {

	private static String	TAG	= "Start Activity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.activity_start);
		BugSenseHandler.initAndStartSession(this, "2bccbeb2");
	}

	@Override
	protected void onStart() {
		super.onStart();

		new ConfigFetchTask().execute(getString(R.string.config_url));
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	private void startNextActivity() {
		Account[] accounts = ((Cloudsdale) getApplication())
				.getSessionManager().getAccounts();

		Intent intent = new Intent();
		if (accounts.length > 0) {
			intent.setClass(this, CloudsdaleActivity.class);
		} else {
			intent.setClass(this, LoginActivity.class);
		}
		startActivity(intent);
	}

	private void showCritialErrorDialog(String title, String message) {
		new AlertDialog.Builder(StartActivity.this)
				.setMessage(message)
				.setTitle(title)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								StartActivity.this.finish();
							}
						}).create().show();
	}

	class ConfigFetchTask extends AsyncTask<String, Void, JsonObject> {

		@Override
		protected JsonObject doInBackground(String... args) {
			val url = args[0];
			val httpClient = AndroidHttpClient.newInstance(
					"cloudsdale-android", StartActivity.this);
			val get = new HttpGet(url);
			try {
				val response = httpClient.execute(get);
				val bodyString = EntityUtils.toString(response.getEntity());
				val json = ((Cloudsdale) getApplication())
						.getJsonDeserializer().fromJson(bodyString,
								JsonObject.class);
				return json;
			} catch (IOException e) {
				BugSenseHandler.sendException(e);
				cancel(true);
			}
			return null;
		}

		@Override
		protected void onCancelled() {
			Log.d(TAG, "Config task cancel called");
			showCritialErrorDialog(getString(R.string.config_error_title),
					getString(R.string.config_error_message));
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(JsonObject result) {
			Log.d(TAG, "Config task completed");
			((Cloudsdale) getApplication()).configureFromRemote(result);
			showCritialErrorDialog(getString(R.string.config_error_title),
					getString(R.string.config_error_message));
			// TODO Implement successful config retrieval for real
			super.onPostExecute(result);
		}

	}
}
