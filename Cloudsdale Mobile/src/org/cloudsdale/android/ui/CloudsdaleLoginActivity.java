package org.cloudsdale.android.ui;

import org.cloudsdale.android.CloudsdaleMobileApplication;
import org.cloudsdale.android.R;
import org.cloudsdale.android.authentication.CloudsdaleAsyncAuth;
import org.cloudsdale.android.authentication.LoginBundle;
import org.cloudsdale.android.models.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

public class CloudsdaleLoginActivity extends SherlockActivity {

	@SuppressWarnings("unused")
	private static final String	TAG	= "CloudsdaleLoginActivity";

	private EditText			usernameField;
	private EditText			passwordField;
	private Button				submitButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Data bind the view objects
		setContentView(R.layout.login_view_cloudsdale_fields);
		usernameField = (EditText) findViewById(R.id.cloudsdaleLoginUsernameField);
		passwordField = (EditText) findViewById(R.id.cloudsdaleLoginPasswordField);
		submitButton = (Button) findViewById(R.id.cloudsdaleLoginSubmitButton);

		getSherlock().setProgressBarIndeterminateVisibility(false);

		// Start the authorization flow when the user clicks the button
		submitButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String username = usernameField.getText().toString();
				String password = passwordField.getText().toString();
				String loginUrl = getString(R.string.cloudsdale_api_url)
						+ "sessions";

				new Auth().execute(new LoginBundle(username, password,
						loginUrl, getString(R.string.cloudsdale_auth_token),
						null));

			}
		});
	}

	/**
	 * Helper method to forward the app to the main view activity
	 */
	private void forwardToMainView() {
		Intent intent = new Intent();
		intent.setClass(this, MainViewActivity.class);
		startActivity(intent);
	}

	private class Auth extends CloudsdaleAsyncAuth {
		@Override
		protected void onCancelled(User result) {
			super.onCancelled(result);
			Toast.makeText(CloudsdaleLoginActivity.this,
					"There was an error logging in, please try again",
					Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onPostExecute(User result) {
			super.onPostExecute(result);

			forwardToMainView();
		}
	}
}
