package org.cloudsdale.android.ui;

import android.accounts.Account;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.bugsense.trace.BugSenseHandler;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;

import java.io.IOException;

import lombok.val;

public class StartActivity extends SherlockActivity {

	private static String	TAG				= "Start Activity";
	private static String	APP_STORE_KEY	= "app_store_url";
	private String			playStoreUrl;

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
		new AlertDialog.Builder(this)
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

	private void showUpdateDialog() {
		new AlertDialog.Builder(this)
				.setMessage(
						"Would you like to go to the Play Store and update Cloudsdale?")
				.setTitle("Cloudsdale is out of date")
				.setPositiveButton("Update",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(Intent.ACTION_VIEW,
										Uri.parse(playStoreUrl));
								startActivity(intent);
							}
						})
				.setNegativeButton("Exit",
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
				val bodyString = EntityUtils.toString(response.getEntity()).replaceAll("\n", "");
				val json = new JsonParser().parse(bodyString).getAsJsonObject();
				playStoreUrl = json.get(APP_STORE_KEY).getAsString();
				return json;
			} catch (IOException e) {
				BugSenseHandler.sendException(e);
				cancel(true);
			}
			return null;
		}

		@Override
		protected void onCancelled() {
			showCritialErrorDialog(getString(R.string.config_error_title),
					getString(R.string.config_error_message));
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(JsonObject result) {
			int currentVersion;
			try {
				currentVersion = getPackageManager().getPackageInfo(
						"org.cloudsdale.android", 0).versionCode;
				if (result.get("minimum_version").getAsInt() > currentVersion) {
					showUpdateDialog();
				} else {
					startNextActivity();
				}
			} catch (NameNotFoundException e) {
				// This can't happen
			}
			super.onPostExecute(result);
		}

	}
}
