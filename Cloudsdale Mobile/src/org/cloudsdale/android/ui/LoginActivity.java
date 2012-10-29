package org.cloudsdale.android.ui;

import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.WazaBe.HoloEverywhere.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.bugsense.trace.BugSenseHandler;
import com.google.gson.JsonObject;
import com.zubhium.ZubhiumSDK;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.managers.UserAccountManager;
import org.cloudsdale.android.managers.UserManager;
import org.cloudsdale.android.models.LoggedUser;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.exceptions.QueryException;
import org.cloudsdale.android.models.queries.SessionQuery;

import java.util.concurrent.ExecutionException;

/**
 * Controller for the login view
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 *         Copyright (c) 2012 Cloudsdale.org
 */
public class LoginActivity extends SherlockActivity {

	public static final String	TAG	= "Cloudsdale LoginViewActivity";

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
	 * Hide the soft keyboard if it's showing
	 */
	private void hideSoftKeyboard() {
		if (mEmailView.hasFocus()) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);
		} else if (mPasswordView.hasFocus()) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
		}
	}

	/**
	 * Stores the user account, both in the account manager and in the SQL
	 * database
	 * 
	 * @param user
	 *            The user that has logged in
	 */
	private boolean storeAccount(LoggedUser user) {
		boolean accountCreated = UserAccountManager.storeAccount(user);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if (accountCreated) {
				AccountAuthenticatorResponse response = extras
						.getParcelable(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
				Bundle result = new Bundle();
				result.putString(AccountManager.KEY_ACCOUNT_NAME,
						user.getName());
				result.putString(AccountManager.KEY_ACCOUNT_TYPE,
						getString(R.string.account_type));
				response.onResult(result);
			}
		}
		return UserManager.storeLoggedInUser(user);
	}

	/**
	 * Sends Cloudsdale credentials to Cloudsdale
	 */
	private void startCloudsdaleAuthentication() {
		// Get inputted email and password
		mEmail = this.mEmailView.getText().toString();
		mPassword = this.mPasswordView.getText().toString();

		if (mEmail == null || mEmail.length() == 0) {
			mEmailView.setError("Email can't be empty");
		} else if (mPassword == null || mPassword.length() == 0) {
			mPasswordView.setError("Password can't be empty");
		} else {
			hideSoftKeyboard();
			showProgress(true);
			CloudsdaleAuthTask auth = new CloudsdaleAuthTask();
			auth.execute();
			try {
				LoggedUser user = auth.get();
				boolean success = storeAccount(user);
				if (success) {
					Intent intent = new Intent();
					intent.setClass(this, HomeActivity.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(this,
							"There was an error logging in, please try again",
							Toast.LENGTH_SHORT).show();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Helper class to asynchronously log the user in using Cloudsdale
	 * credentials
	 * 
	 * @author Jamison Greeley (atomicrat2552@gmail.com)
	 *         Copyright (c) 2012 Cloudsdale.org
	 * 
	 */
	private class CloudsdaleAuthTask extends AsyncTask<Void, Void, LoggedUser> {

		@Override
		protected LoggedUser doInBackground(Void... params) {
			String sessionUrl = getString(R.string.cloudsdale_api_base)
					+ getString(R.string.cloudsdale_sessions_endpoint);
			QueryData data = new QueryData();
			SessionQuery query = new SessionQuery(sessionUrl);

			JsonObject json = new JsonObject();
			json.addProperty("email", mEmail);
			json.addProperty("password", mPassword);
			data.setJson(json.toString());

			if (Cloudsdale.DEBUG) {
				Log.d(TAG, "Json: " + json);
			}

			try {
				return query.execute(data, LoginActivity.this);
			} catch (QueryException e) {
				if (e.getErrorCode() == 500)
					mEmailView.setError("Something went wrong during login");
				else if (e.getErrorCode() == 404)
					mEmailView.setError("Couldn't connect to Cloudsdale");
				return null;
			}
		}

		@Override
		protected void onCancelled() {
			showProgress(false);
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(LoggedUser result) {
			showProgress(false);
			super.onPostExecute(result);
		}

	}
}