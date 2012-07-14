package org.cloudsdale.android.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.webkit.WebView;
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
import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthGetTemporaryToken;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.gson.Gson;

import org.cloudsdale.android.PersistentData;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.LoggedUser;
import org.cloudsdale.android.models.authentication.CloudsdaleAsyncAuth;
import org.cloudsdale.android.models.authentication.LoginBundle;
import org.cloudsdale.android.models.authentication.OAuthBundle;
import org.cloudsdale.android.models.authentication.Provider;
import org.cloudsdale.android.models.network_models.FacebookResponse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Controller for the login view
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class LoginActivity extends SherlockActivity {
	public static final String	TAG						= "Cloudsdale LoginViewActivity";
	public static final int		FACEBOOK_ACTIVITY_CODE	= 10298;

	@SuppressWarnings("unused")
	private static final String	FILENAME				= "AndroidSSO_data";

	private EditText			emailField;
	private EditText			passwordField;
	private Button				cdButton;
	private Button				fbButton;
	private Button				twitterButton;
	private ProgressDialog		progress;

	private Facebook			facebook;
	private SharedPreferences	mPrefs;

	// Fields for Facebook login;
	private boolean				fbRunnerWorking;
	private String				fbId;

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
				sendCdCredentials();
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
		// Create the register menu option
		menu.add("Register")
				.setIcon(R.drawable.ic_register_user)
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

	@Override
	protected void onResume() {
		super.onResume();
		WebView webview = new WebView(this);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setVisibility(View.VISIBLE);
		setContentView(webview);

		final OAuthHmacSigner signer = new OAuthHmacSigner();
		signer.clientSharedSecret = getString(R.string.twitter_consumer_secret);

		OAuthGetTemporaryToken temporaryToken = new OAuthGetTemporaryToken(
				getString(R.string.twitter_request_url));
		temporaryToken.transport = new ApacheHttpTransport();
		temporaryToken.signer = signer;
		temporaryToken.consumerKey = getString(R.string.twitter_consumer_key);
		temporaryToken.callback = getString(R.string.twitter_callback_url);

		OAuthCredentialsResponse tempCredentials;
		try {
			tempCredentials = temporaryToken.execute();
			signer.tokenSharedSecret = tempCredentials.tokenSecret;

			OAuthAuthorizeTemporaryTokenUrl authorizeUrl = new OAuthAuthorizeTemporaryTokenUrl(
					getString(R.string.twitter_auth_url));
			authorizeUrl.temporaryToken = tempCredentials.token;
			String authorizationUrl = authorizeUrl.build();

			webview.loadUrl(authorizationUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showDialog() {
		progress = ProgressDialog.show(this, "",
				getString(R.string.login_dialog_wait_string));
	}

	public void cancelDialog() {
		if (progress != null && progress.isShowing()) {
			progress.dismiss();
		}
	}

	public void startFbAuthFlow() {
		// Show the dialog
		showDialog();

		new Thread(new Runnable() {

			@Override
			public void run() {
				Looper.prepare();

				// Get existing token if it exists
				mPrefs = getPreferences(MODE_PRIVATE);
				String access_token = mPrefs.getString("access_token", null);
				long expires = mPrefs.getLong("access_expires", 0);

				// Set items
				if (access_token != null) {
					facebook.setAccessToken(access_token);
				}
				if (expires != 0) {
					facebook.setAccessExpires(expires);
				}

				// Only call auth if access has expired
				if (!facebook.isSessionValid()) {
					facebook.authorize(LoginActivity.this, new String[] {},
							new FbListener());
				}

				// Start CD Auth flow
				getFbUserId();

				// Chill while FB does its thing
				while (fbRunnerWorking) {
					continue;
				}

				// Send the credentials to Cloudsdale
				sendFbCredentials();
			}
		}).start();
	}

	public void startTwitterAuthFlow() {
	}

	public void getFbUserId() {
		// Get the user's uid from FB
		fbRunnerWorking = true;
		FbAsyncRunner runner = new FbAsyncRunner(facebook);
		runner.request("me?fields=id", new Bundle(), new FbAsyncListener());
	}

	public void sendCdCredentials() {
		// Get inputted email and password
		String email = emailField.getText().toString();
		String pass = passwordField.getText().toString();

		// Check that neither is null
		if (email != null && !email.equals("") && pass != null
				&& !pass.equals("")) {
			showDialog();

			// Get the api resources
			Resources res = getResources();
			String apiUrl = res.getString(R.string.api_base)
					+ res.getString(R.string.sessions_endpoint);

			// Build the login bundle and execute auth
			LoginBundle login = new LoginBundle(email, pass, apiUrl, null, null);
			Auth auth = new Auth();
			auth.execute(login);
		} else {
			Toast.makeText(
					this,
					"You must fill out both the email and password fields to log in",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void sendFbCredentials() {
		// Create the oAuth bundle
		OAuthBundle oAuth = new OAuthBundle(Provider.FACEBOOK, fbId,
				getString(R.string.cloudsdale_auth_token));

		// Build the login bundle with the oAuth bundle
		LoginBundle bundle = new LoginBundle(null, null,
				getString(R.string.cloudsdale_api_url) + "sessions",
				getString(R.string.cloudsdale_auth_token), oAuth);

		// Send the credentials to Cloudsdale
		Auth auth = new Auth();
		auth.execute(bundle);
	}

	private class Auth extends CloudsdaleAsyncAuth {

		@Override
		protected void onCancelled(LoggedUser result) {
			super.onCancelled(result);
			LoginActivity.this.cancelDialog();
			Toast.makeText(LoginActivity.this,
					"There was an error logging in, please try again",
					Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onPostExecute(LoggedUser result) {
			LoginActivity.this.cancelDialog();

			if (result != null) {
				// Store the user
				PersistentData.StoreMe(LoginActivity.this, result);

				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, CloudListActivity.class);
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

	private class FbListener implements Facebook.DialogListener {

		@Override
		public void onComplete(Bundle values) {
			SharedPreferences.Editor editor = mPrefs.edit();
			editor.putString("access_token", facebook.getAccessToken());
			editor.putLong("access_expires", facebook.getAccessExpires());
			editor.commit();
		}

		@Override
		public void onFacebookError(FacebookError e) {
			BugSenseHandler.log(TAG, e);
			cancelDialog();
			Toast.makeText(
					LoginActivity.this,
					"There was an error communicating with Facebook, please try again",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onError(DialogError e) {
			cancelDialog();
			Toast.makeText(LoginActivity.this,
					"Facebook encountered an error,  please try again",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			cancelDialog();
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

			// Deserialize the response
			Gson gson = new Gson();
			FacebookResponse fbr = gson.fromJson(response,
					FacebookResponse.class);

			// Set the fields
			LoginActivity.this.fbId = fbr.getId();
			LoginActivity.this.fbRunnerWorking = false;
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