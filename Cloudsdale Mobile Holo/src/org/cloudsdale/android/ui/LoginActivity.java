package org.cloudsdale.android.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

/**
 * The activity controller for the initial login
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class LoginActivity extends Activity {
	private Button cloudsdaleLoginButton;
	private Button facebookLoginButton;
	private Button twitterLoginButton;

	private SharedPreferences sharedPrefs;

	private Facebook fb;
	private String fbSelf;
	private String FILENAME;

	/**
	 * Called when the activity is first created.
	 * 
	 * @see android.app.Activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login);

		FILENAME = "FB_SSO_data";

		// Fetch the main GUI widgets
		cloudsdaleLoginButton = (Button) findViewById(R.id.cloudsdale_auth_button);
		facebookLoginButton = (Button) findViewById(R.id.facebook_auth_button);
		twitterLoginButton = (Button) findViewById(R.id.twitter_auth_button);
		
		// Disable oAuth buttons. No API support yet
		facebookLoginButton.setEnabled(false);
		twitterLoginButton.setEnabled(false);

		// Check for an existing session
		sharedPrefs = getPreferences(MODE_PRIVATE);
		if (sharedPrefs.getString("me", null) != null) {
			Intent intent = new Intent();
			intent.setClass(this, MainViewActivity.class);
			startActivity(intent);
		}

		// Create the Facebook SDK object
		fb = new Facebook(getString(R.string.facebook_dev_app_id));

		// Set the button listeners
		cloudsdaleLoginButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				doCloudsdaleAuth();
			}
		});

		facebookLoginButton.setOnClickListener(new OnClickListener() {

			// Check to see if Facebook is installed. If not, prompt the user to
			// install the Facebook app
			public void onClick(View v) {
				if (checkFacebookInstalled())
					authFacebook();
				else {
					promptForFacebookInstall();
				}
			}
		});

		twitterLoginButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				doTwitterAuth();
			}
		});

	}

	/**
	 * Respond to orientation changes
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		this.setContentView(R.layout.login);
	}

	/**
	 * Login using Cloudsdale authentication via a dedicated Cloudsdale login
	 * activity
	 */
	private void doCloudsdaleAuth() {
		Intent intent = new Intent();
		intent.setClass(LoginActivity.this, CloudsdaleLoginActivity.class);
		startActivity(intent);
	}

	/**
	 * Check to see if the user is authenticated via Facebook
	 */
	private void checkFacebookAuth() {
		String access_token = sharedPrefs.getString("access_token", null);
		long expires = sharedPrefs.getLong("access_expires", 0);
		if (access_token != null) {
			fb.setAccessToken(access_token);
		}
		if (expires != 0) {
			fb.setAccessExpires(expires);
		}

		if (!fb.isSessionValid()) {
			fb.authorize(this, new String[] {}, new DialogListener() {
				public void onComplete(Bundle values) {
					SharedPreferences.Editor editor = sharedPrefs.edit();
					editor.putString("access_token", fb.getAccessToken());
					editor.putLong("access_expires", fb.getAccessExpires());
					editor.commit();
				}

				public void onFacebookError(FacebookError error) {
					/** TODO handle FB errors **/
				}

				public void onError(DialogError e) {
					/** TODO handle dialog errors **/
				}

				public void onCancel() {
					/** TODO handle user canceling login **/
				}
			});
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(
				LoginActivity.this).setMessage(
				fb.getAccessToken() + " " + fb.getAccessExpires())
				.setNegativeButton("OK", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/**
	 * Prompt the user to install the Facebook application
	 */
	private void promptForFacebookInstall() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				LoginActivity.this)
				.setMessage(R.string.facebook_install_prompt)
				.setPositiveButton(R.string.generic_positive_button,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(Intent.ACTION_VIEW);
								intent.setData(Uri
										.parse("market://details?id=com.facebook.katana"));
								startActivity(intent);
							}
						})
				.setNegativeButton(R.string.generic_negative_button,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/**
	 * Authenticate the user via Facebook
	 */
	private void authFacebook() {
		checkFacebookAuth();
		getFacebookData();
	}

	/**
	 * Query Facebook for the logged in user
	 */
	private void getFacebookData() {
		AsyncFacebookRunner async = new AsyncFacebookRunner(fb);
		async.request("me", new AsyncFacebookRunner.RequestListener() {
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
			}

			public void onIOException(IOException e, Object state) {
			}

			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
			}

			public void onFacebookError(FacebookError e, Object state) {
			}

			public void onComplete(String response, Object state) {
				getMe(response);
			}
		});

		while (fbSelf == null)
			;

		AlertDialog.Builder builder = new AlertDialog.Builder(
				LoginActivity.this).setMessage(fbSelf).setNegativeButton("OK",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/**
	 * Authenticate the user via Twitter SSO
	 * 
	 * @param me
	 */

	private void getMe(String me) {
		fbSelf = me;
	}

	private void doTwitterAuth() {
		// TODO Implement Twitter login
	}

	/**
	 * Respond to activity results
	 */

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		fb.authorizeCallback(requestCode, resultCode, data);
	}

	/**
	 * Check the system to see if the Facebook application is installed
	 * 
	 * @return Whether the Facebook application is installed on the system
	 */
	public boolean checkFacebookInstalled() {
		boolean installed = false;

		try {
			ApplicationInfo info = getPackageManager().getApplicationInfo(
					"com.facebook.katana", 0);
			installed = true;
		} catch (PackageManager.NameNotFoundException e) {
		}

		return installed;
	}
	
	/**
	 * Fetch the request token from Twitter
	 */
	public Pair<String,String> getTwitterRequest() {
		
		return null;
	}

	/**
	 * Handle the app resuming for some reason
	 */
	@Override
	protected void onResume() {
		super.onResume();
		fb.extendAccessTokenIfNeeded(this, null);
	}
	
	private class AsyncAuth extends AsyncTask<String, String, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... arg0) {
				// Create the HTTP args
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
				
				return null;
		}
		
	}
}