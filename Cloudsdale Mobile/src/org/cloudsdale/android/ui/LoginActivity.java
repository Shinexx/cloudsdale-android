package org.cloudsdale.android.ui;

import org.cloudsdale.android.R;
import org.cloudsdale.android.authentication.CloudsdaleAsyncAuth;
import org.cloudsdale.android.authentication.LoginBundle;
import org.cloudsdale.android.logic.PersistentData;
import org.cloudsdale.android.models.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.bugsense.trace.BugSenseHandler;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

/**
 * Controller for the login view
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class LoginActivity extends SherlockActivity {
	public static final String TAG = "Cloudsdale LoginViewActivity";
	public static final int FACEBOOK_ACTIVITY_CODE = 10298;

	private static final String FILENAME = "AndroidSSO_data";

	private EditText emailField;
	private EditText passwordField;
	private Button cdButton;
	private Button fbButton;
	private Button twitterButton;
	private ProgressDialog progress;

	private Facebook facebook = new Facebook(
			getString(R.string.facebook_api_token));
	private SharedPreferences mPrefs;

	/**
	 * Lifetime method for the creation of the controller
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// SET THEMES HERE
		super.onCreate(savedInstanceState);

		// Hide the Cloudsdale text in the ActionBar
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		// Set the layout
		setContentView(R.layout.login_view);

		// Setup BugSense
		BugSenseHandler.setup(this, "2bccbeb2");

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
			facebook.authorize(this, new String[] { "email" }, new FbDialogListener());
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
			showDialog();
		}

		@Override
		protected void onCancelled(User result) {
			super.onCancelled(result);
			Toast.makeText(LoginActivity.this,
					"There was an error logging in, please try again",
					Toast.LENGTH_LONG);

			cancelDialog();
		}

		@Override
		protected void onPostExecute(User result) {
			if (result != null) {
				PersistentData.setMe(result);
			} else {
				Toast.makeText(LoginActivity.this,
						"There was an error, please try again",
						Toast.LENGTH_LONG);
			}

			cancelDialog();
		}
	}

	public class FbDialogListener implements Facebook.DialogListener {

		@Override
		public void onComplete(Bundle values) {
			SharedPreferences.Editor editor = mPrefs.edit();
			editor.putString("access_token", facebook.getAccessToken());
			editor.putLong("access_expires", facebook.getAccessExpires());
			editor.commit();
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
}