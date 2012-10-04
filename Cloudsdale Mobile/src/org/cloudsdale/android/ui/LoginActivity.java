package org.cloudsdale.android.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.bugsense.trace.BugSenseHandler;
import com.zubhium.ZubhiumSDK;

import org.cloudsdale.android.R;
import org.cloudsdale.android.models.authentication.CloudsdaleAsyncAuth;

/**
 * Controller for the login view
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 *         Copyright (c) 2012 Cloudsdale.org
 */
public class LoginActivity extends SherlockActivity {

	public static final String	TAG			= "Cloudsdale LoginViewActivity";

	// Login progress
	private CloudsdaleAsyncAuth	mAuthTask	= null;

	// Fields
	private String				mEmail;
	private String				mPassword;

	// UI references
	private EditText			mEmailView;
	private EditText			mPasswordView;
	private View				mLoginFormView;
	private View				mLoginStatusView;
	private TextView			mLoginStatusMessageView;

	@Override
	protected void onCreate(Bundle icicle) {
		// Setup BugSense
		BugSenseHandler.setup(this, "2bccbeb2");

		// Setup Zubhium
		ZubhiumSDK sdk = ZubhiumSDK.getZubhiumSDKInstance(
				getApplicationContext(), "65de0ea209fa414beee8518bd08b05");
		sdk.enableCrashReporting(true);

		// Hide the Cloudsdale text and icon in the ActionBar
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);

		// Make the super call
		super.onCreate(icicle);

		// Set the layout
		setContentView(R.layout.activity_login);

		// Setup the login form
		mEmailView = (EditText) findViewById(R.id.email);
		mPasswordView = (EditText) findViewById(R.id.password);
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							startCloudsdaleAuthentication();
							return true;
						}
						return false;
					}
				});

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						startCloudsdaleAuthentication();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getSupportMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Sends Cloudsdale credentials to Cloudsdale
	 */
	private void startCloudsdaleAuthentication() {
		// Get inputted email and password
		mEmail = this.mEmailView.getText().toString();
		mPassword = this.mPasswordView.getText().toString();

	}
}