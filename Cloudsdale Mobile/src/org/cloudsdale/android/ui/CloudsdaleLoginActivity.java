package org.cloudsdale.android.ui;

import org.cloudsdale.android.R;
import org.cloudsdale.android.authentication.CloudsdaleAsyncAuth;
import org.cloudsdale.android.authentication.LoginBundle;
import org.cloudsdale.android.logic.PersistentData;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
import com.googlecode.androidannotations.annotations.Background;

public class CloudsdaleLoginActivity extends SherlockActivity {

	private static final String	TAG	= "CloudsdaleLoginActivity";

	private EditText			usernameField;
	private EditText			passwordField;
	private Button				submitButton;
	private Handler				handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Request an indeterminate progress bar in the Sherlock bar
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

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

				new CloudsdaleAsyncAuth().execute(new LoginBundle(username,
						password, loginUrl,
						getString(R.string.cloudsdale_auth_token)));

				// Show a progress dialog while login is happening
				getSherlock().setProgressBarIndeterminateVisibility(true);

				// Since login is async, keep checking the user object until it
				// returns a value then cancel the progress dialog
				handler = new Handler();
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						boolean running = true;

						while (running) {
							if (PersistentData.getMe() != null) {
								getSherlock()
										.setProgressBarIndeterminateVisibility(
												false);
								running = false;
								// TODO Forward to the main view
							} else {
								handler.postDelayed(this, 500);
							}
						}
					}
				}, 500);
			}
		});
	}
}
