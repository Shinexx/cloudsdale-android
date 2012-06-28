package org.cloudsdale.android.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.cloudsdale.android.PersistentData;
import org.cloudsdale.android.R;
import org.cloudsdale.android.authentication.CloudsdaleAsyncAuth;
import org.cloudsdale.android.authentication.LoginBundle;
import org.cloudsdale.android.authentication.OAuthBundle;
import org.cloudsdale.android.authentication.Provider;
import org.cloudsdale.android.models.FacebookResponse;
import org.cloudsdale.android.models.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.bugsense.trace.BugSenseHandler;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.google.gson.Gson;

/**
 * Controller for the login view
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class LoginActivity extends SherlockActivity {
	public static final String			TAG						= "Cloudsdale LoginViewActivity";
	public static final int				FACEBOOK_ACTIVITY_CODE	= 10298;

	@SuppressWarnings("unused")
	private static final String			FILENAME				= "AndroidSSO_data";

	private EditText					emailField;
	private EditText					passwordField;
	private Button						cdButton;
	private Button						fbButton;
	private Button						twitterButton;
	private ProgressDialog				progress;

	private Facebook					facebook;
	private SharedPreferences			mPrefs;
	private CommonsHttpOAuthConsumer	httpOauthConsumer;
	private OAuthProvider				httpOauthProvider;

	/**
	 * Lifetime method for the creation of the controller
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// SET THEMES HERE

		// Hide the Cloudsdale text and icon in the ActionBar
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);

		super.onCreate(savedInstanceState);

		// Set the layout
		setContentView(R.layout.login_view);

		// Instantiate the Facebook client
		facebook = new Facebook(getString(R.string.facebook_api_token));

		// Bind the data entities
		emailField = (EditText) findViewById(R.id.login_email_field);
		passwordField = (EditText) findViewById(R.id.login_password_field);
		cdButton = (Button) findViewById(R.id.login_view_cd_button);
		fbButton = (Button) findViewById(R.id.login_view_fb_button);
		twitterButton = (Button) findViewById(R.id.login_view_twitter_button);

		// Set the CD button listener
		cdButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String email = emailField.getText().toString();
				String pass = passwordField.getText().toString();
				if (email != null && !email.equals("") && pass != null
						&& !pass.equals("")) {
					new Auth()
							.execute(new LoginBundle(email, pass,
									getString(R.string.cloudsdale_api_url)
											+ "sessions", null, null));
				}
			}
		});

		// Set the FB button listener
		fbButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startFbAuthFlow();
			}
		});

		// Set the Twitter button listener
		twitterButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startTwitterAuthFlow();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Register")
				.setIcon(R.drawable.user_add)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Callback to Facebook
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	protected void onResume(Intent intent) {
		super.onNewIntent(intent);

		Uri uri = intent.getData();
		showDialog();

		// Handle twitter login when url is for Twitter
		if (uri != null
				&& uri.toString().startsWith(
						getString(R.string.twitter_callback_url))) {
			String verifier = uri
					.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);

			try {
				httpOauthProvider.retrieveAccessToken(httpOauthConsumer,
						verifier);
				String userKey = httpOauthConsumer.getToken();
				String userSecret = httpOauthConsumer.getTokenSecret();

				Log.d(TAG, "User key: " + userKey);
				Log.d(TAG, "User secret: " + userSecret);
				// Do CD login stuff here
			} catch (Exception e) {
				Toast.makeText(this, "There was an error, please try again",
						Toast.LENGTH_LONG).show();
				BugSenseHandler.log(TAG, e);
			}
		}
	}

	public void startFbAuthFlow() {
		// Get existing token if it exists
		mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);
		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}
		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		// Only call auth if access has expired
		if (!facebook.isSessionValid()) {
			facebook.authorize(this, new String[] { "email" }, new FbListener());
		} else {
			// Start CD Auth flow
			startCdAuthFromFb();
		}

	}

	public void startCdAuthFromFb() {
		// Show the dialog
		showDialog();

		// Get the user's uid from FB
		FbAsyncRunner runner = new FbAsyncRunner(facebook);
		runner.request("me", new Bundle(), new FbAsyncListener());
	}

	public void sendFbCredentials(LoginBundle bundle) {
		Auth auth = new Auth();
		auth.execute(bundle);

		while (auth.getStatus() == AsyncTask.Status.RUNNING) {
			continue;
		}

		cancelDialog();
	}

	public void startTwitterAuthFlow() {
		try {
			// Create OAuth objects
			httpOauthConsumer = new CommonsHttpOAuthConsumer(
					getString(R.string.twitter_consumer_key),
					getString(R.string.twitter_consumer_secret));
			httpOauthProvider = new DefaultOAuthProvider(
					getString(R.string.twitter_request_url),
					getString(R.string.twitter_access_token_url),
					getString(R.string.twitter_auth_url));

			// Get the request token and generate the login url
			String authUrl = "";
			AsyncTwitter twit = new AsyncTwitter();
			authUrl = twit.doInBackground(new String[] { authUrl });

			// Fire up the browser and get do oAuth
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
		} catch (Exception e) {
			Toast.makeText(this, "There was an error, please try again",
					Toast.LENGTH_LONG).show();
			BugSenseHandler.log(TAG, e);
		}
	}

	public void showDialog() {
		progress = ProgressDialog.show(this, "",
				getString(R.string.login_dialog_wait_string));
	}

	public void cancelDialog() {
		if (progress.isShowing()) {
			progress.dismiss();
		}
	}

	public class Auth extends CloudsdaleAsyncAuth {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			LoginActivity.this.showDialog();
		}

		@Override
		protected void onCancelled(User result) {
			super.onCancelled(result);
			LoginActivity.this.cancelDialog();
			Toast.makeText(LoginActivity.this,
					"There was an error logging in, please try again",
					Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onPostExecute(User result) {
			LoginActivity.this.cancelDialog();

			if (result != null) {
				PersistentData.StoreMe(result, LoginActivity.this);
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, MainViewActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			} else {
				Toast.makeText(LoginActivity.this,
						"There was an error, please try again",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private class AsyncTwitter extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				params[0] = httpOauthProvider.retrieveRequestToken(
						httpOauthConsumer,
						getString(R.string.twitter_callback_url));
			} catch (OAuthMessageSignerException e) {
				BugSenseHandler.log(TAG, e);
			} catch (OAuthNotAuthorizedException e) {
				BugSenseHandler.log(TAG, e);
			} catch (OAuthExpectationFailedException e) {
				BugSenseHandler.log(TAG, e);
			} catch (OAuthCommunicationException e) {
				BugSenseHandler.log(TAG, e);
			}
			Log.d(TAG, params[0]);

			return params[0];
		}
	}

	private class FbListener implements Facebook.DialogListener {

		@Override
		public void onComplete(Bundle values) {
			SharedPreferences.Editor editor = mPrefs.edit();
			editor.putString("access_token", facebook.getAccessToken());
			editor.putLong("access_expires", facebook.getAccessExpires());
			editor.commit();
			startCdAuthFromFb();
		}

		@Override
		public void onFacebookError(FacebookError e) {

		}

		@Override
		public void onError(DialogError e) {

		}

		@Override
		public void onCancel() {

		}

	}

	private class FbAsyncRunner extends AsyncFacebookRunner {

		public FbAsyncRunner(Facebook fb) {
			super(fb);
		}

	}

	private class FbAsyncListener implements
			AsyncFacebookRunner.RequestListener {

		@Override
		public void onComplete(String response, Object state) {
			Looper.prepare();
			Gson gson = new Gson();
			FacebookResponse fbr = gson.fromJson(response,
					FacebookResponse.class);
			String id = fbr.getId();
			OAuthBundle oAuth = new OAuthBundle(Provider.FACEBOOK, id,
					getString(R.string.cloudsdale_auth_token));
			LoginBundle bundle = new LoginBundle(null, null,
					getString(R.string.cloudsdale_api_url) + "sessions",
					getString(R.string.cloudsdale_auth_token), oAuth);

			sendFbCredentials(bundle);
		}

		@Override
		public void onIOException(IOException e, Object state) {

		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {

		}

		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object state) {

		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {

		}

	}
}