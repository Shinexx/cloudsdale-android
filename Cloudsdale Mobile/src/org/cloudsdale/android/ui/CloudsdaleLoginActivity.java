package org.cloudsdale.android.ui;

import org.cloudsdale.android.CloudsdaleMobileApplication;
import org.cloudsdale.android.R;
import org.cloudsdale.android.authentication.CloudsdaleAsyncAuth;
import org.cloudsdale.android.authentication.LoginBundle;
import org.cloudsdale.android.logic.PersistentData;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;

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
		setContentView(R.layout.cloudsdale_login_fields);
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

	private class Auth extends CloudsdaleAsyncAuth {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CloudsdaleLoginActivity.this);
			dialog.setMessage(CloudsdaleMobileApplication.getContext()
					.getString(R.string.login_dialog_wait_string));
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();
		}
	}
}
