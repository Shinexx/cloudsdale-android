package org.cloudsdale.android.ui;

import org.cloudsdale.android.authentication.CloudsdaleAsyncAuth;
import org.cloudsdale.android.logic.PersistentData;
import org.cloudsdale.android.models.User;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

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

	// Progress dialog to show while authentication is occurring
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
				postAuth(PersistentData.getMe());
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

	/**
	 * Handles post-authentication logic
	 * 
	 * @param user
	 *            The user that has been authenticated
	 */
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
}