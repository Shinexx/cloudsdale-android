package org.cloudsdale.android.ui.fragments;

import android.accounts.Account;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.DataStore;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.network.SessionResponse;
import org.cloudsdale.android.ui.ActivityCallbacks;
import org.cloudsdale.android.ui.CloudsdaleActivity;
import org.json.JSONException;
import org.json.JSONObject;

@EFragment(R.layout.fragment_login)
public class LoginFragment extends Fragment {

	private static final String	TAG	= "Cloudsdale Login Fragment";

	private String				email;
	private String				password;
	private ActivityCallbacks	callbacks;

	@ViewById(R.id.email)
	EditText					emailView;
	@ViewById(R.id.password)
	EditText					passwordView;
	@ViewById(R.id.login_form)
	View						loginFormView;
	@ViewById(R.id.login_status)
	View						loginStatusView;
	@ViewById(R.id.login_status_message)
	TextView					loginStatusMessageView;
	@App
	Cloudsdale					cloudsdale;

	@Override
	public void onAttach(Activity activity) {
		callbacks = (ActivityCallbacks) activity;
		super.onAttach(activity);
	}

	@AfterViews
	void addViewBehaviour() {
		passwordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(android.widget.TextView arg0,
							int textView, KeyEvent keyEvent) {
						int id = keyEvent.getKeyCode();
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});
	}

	@Click(R.id.sign_in_button)
	void signInClick() {
		attemptLogin();
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		// Reset errors.
		emailView.setError(null);
		passwordView.setError(null);

		// Store values at the time of the login attempt.
		email = emailView.getText().toString();
		password = passwordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(password)) {
			passwordView.setError(getString(R.string.error_field_required));
			focusView = passwordView;
			cancel = true;
		} else if (password.length() < 4) {
			passwordView.setError(getString(R.string.error_invalid_password));
			focusView = passwordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			emailView.setError(getString(R.string.error_field_required));
			focusView = emailView;
			cancel = true;
		} else if (!email.contains("@")) {
			emailView.setError(getString(R.string.error_invalid_email));
			focusView = emailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else
			try {
				{
					// Show a progress spinner, and kick off a background task
					// to
					// perform the user login attempt.
					loginStatusMessageView
							.setText(R.string.login_progress_signing_in);
					showProgress(true);
					cloudsdale.callZephyr().postSession(email, password,
							new AsyncHttpClient.JSONObjectCallback() {

								@Override
								public void onCompleted(Exception e,
										AsyncHttpResponse source,
										JSONObject result) {
									boolean error = false;
									if (e != null) {
										error = true;
									}
									processSessionResponse(result.toString(),
											error);
									showProgress(false);
								}
							});
				}
			} catch (JSONException e) {
				showProgress(false);
				((CloudsdaleActivity) getActivity()).displayLoginFailCrouton(e
						.getMessage());
			}
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

			loginStatusView.setVisibility(View.VISIBLE);
			loginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							loginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			loginFormView.setVisibility(View.VISIBLE);
			loginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							loginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			loginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	private void processSessionResponse(String json, boolean error) {
		if (cloudsdale.isDebuggable()) {
			Log.d(TAG, "Received response:\n " + json);
		}
		Gson gson = cloudsdale.getJsonDeserializer();
		SessionResponse response = gson.fromJson(json, SessionResponse.class);
		showProgress(false);
		if (error || response.getErrors().length > 0) {
			Crouton.showText(getActivity(),
					"Error: " + response.getErrors()[0].getMessage(),
					Style.ALERT);
		} else {
			DataStore manager = cloudsdale.getDataStore();
			manager.storeAccount(response.getResult());
			Account[] accounts = manager.getAccounts();
			manager.setActiveAccount(accounts[accounts.length - 1]);
			callbacks.onLoginCompleted();
		}
	}

}
