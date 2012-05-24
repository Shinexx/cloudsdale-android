package org.cloudsdale.android.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.cloudsdale.android.R;
import org.cloudsdale.android.authentication.CloudsdaleAsyncAuth;
import org.cloudsdale.android.authentication.LoginBundle;
import org.cloudsdale.android.authentication.OAuthBundle;
import org.cloudsdale.android.authentication.Provider;
import org.cloudsdale.android.models.FacebookResponse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.google.gson.Gson;

/**
 * Code behind for the Login fragment. Supports FB and Twitter login
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class LoginFragment extends SherlockFragment {

	private final String		TAG			= "Cloudsdale LoginFragment";
	private final String		FILENAME	= "AndroidSSO_data";
	public static Facebook		fb;
	public static Gson			gson;

	private SharedPreferences	mPrefs;

	/**
	 * Lifetime method to create a new instance of the fragment
	 * 
	 * @return a new instance of the fragment
	 */
	static LoginFragment newInstance() {
		return new LoginFragment();
	}

	/**
	 * Lifetime event for creation
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * Lifetime event for creating/inflating the fragment view
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Bind the data objects and inflate the view
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.login_view_option_buttons, container, false);

		// Create the button listeners

		// Handle CD login by forwarding to the new activity
		Button cloudsdaleButton = (Button) layout
				.findViewById(R.id.loginViewCloudsdaleButton);
		cloudsdaleButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(LoginFragment.this.getActivity(),
						CloudsdaleLoginActivity.class);
				startActivity(intent);
			}
		});

		// Hand FB login by calling on FB SDK
		Button facebookButton = (Button) layout
				.findViewById(R.id.loginViewFacebookButton);
		facebookButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startFacebookAuthFlow();
			}
		});

		// Set the layout gravity
		layout.setGravity(Gravity.CENTER);

		return layout;
	}

	/**
	 * Method to start the FB authentication flow through the FB SDK
	 */
	public void startFacebookAuthFlow() {
		// Create the FB instance
		if (fb == null) {
			fb = new Facebook(getString(R.string.facebook_api_token));
		}

		// Fetch stored SSO data
		mPrefs = getActivity().getPreferences(getActivity().MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);

		if (access_token != null) {
			fb.setAccessToken(access_token);
		}
		if (expires != 0) {
			fb.setAccessExpires(expires);
		}

		// Start the auth flow
		if (!fb.isSessionValid()) {
			fb.authorize(LoginFragment.this.getActivity(),
					new String[] { "offline_access" },
					LoginViewActivity.FACEBOOK_ACTIVITY_CODE, new FbListener());
		}
	}

	/**
	 * Method to start the Facebook Graph request
	 */
	private void startGraphRequest() {
		FbRunner runner = new FbRunner(LoginFragment.fb);
		runner.request("me?fields=id", new FbAsyncListener());
	}

	/**
	 * Internal helper class to handle Facebook callback events
	 * 
	 * @author Jamison Greeley (atomicrat2552@gmail.com)
	 * 
	 */
	private class FbListener implements Facebook.DialogListener {

		@Override
		public void onComplete(Bundle values) {
			Log.d(TAG, "Starting Facebook Auth");
			startGraphRequest();
		}

		@Override
		public void onFacebookError(FacebookError e) {
			Toast.makeText(LoginFragment.this.getActivity(),
					"There was an error with Facebook, please try again",
					Toast.LENGTH_LONG).show();
			Log.e(TAG, "Facebook Error: " + e.getMessage());
		}

		@Override
		public void onError(DialogError e) {
			Toast.makeText(LoginFragment.this.getActivity(),
					"There was an error, please try again", Toast.LENGTH_LONG)
					.show();
			Log.e(TAG, "Error during Facebook login: " + e.getMessage());
		}

		@Override
		public void onCancel() {
			// Do nothing, already on login screen
		}
	}

	/**
	 * Internal helper class to listen for async responses from Facebook
	 * 
	 * @author Jamison Greeley (atomicrat2552@gmail.com)
	 * 
	 */
	private class FbAsyncListener implements
			AsyncFacebookRunner.RequestListener {

		@Override
		public void onComplete(String response, Object state) {
			if (gson == null) {
				gson = new Gson();
			}
			FacebookResponse resp = gson.fromJson(response,
					FacebookResponse.class);
			if (resp.getId() == null) {
				Looper.prepare();
				fb.authorize(getActivity(), new FbListener());
				return;
			} else {
				Log.d(TAG, "Facebook response: " + response);
				Log.d(TAG, "Facebook access token: " + fb.getAccessToken());
				OAuthBundle bundle = new OAuthBundle(Provider.FACEBOOK,
						resp.getId(), getString(R.string.cloudsdale_auth_token));
				Looper.prepare();
				LoginBundle lBundle = new LoginBundle(null, null,
						getString(R.string.cloudsdale_api_url) + "sessions",
						getString(R.string.cloudsdale_auth_token), bundle);
				new Auth().execute(new LoginBundle[] { lBundle });
				Log.d(TAG, "Facebook auth Completed");
			}
		}

		@Override
		public void onIOException(IOException e, Object state) {
			Toast.makeText(LoginFragment.this.getActivity(),
					"There was an error, please try again", Toast.LENGTH_LONG)
					.show();
			Log.e(TAG, "IO Exception: " + e.getMessage());
		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
			Toast.makeText(LoginFragment.this.getActivity(),
					"There was an error, please try again", Toast.LENGTH_LONG)
					.show();
			Log.e(TAG, "FileNotFound Exception: " + e.getMessage());
		}

		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object state) {
			Toast.makeText(LoginFragment.this.getActivity(),
					"There was an error, please try again", Toast.LENGTH_LONG)
					.show();
			Log.e(TAG, "MalformedURL Exception: " + e.getMessage());
		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {
			Toast.makeText(LoginFragment.this.getActivity(),
					"There was an error with Facebook, please try again",
					Toast.LENGTH_LONG).show();
			Log.e(TAG, "Facebook Error: " + e.getMessage());
		}

	}

	private class FbServiceListener implements Facebook.ServiceListener {

		@Override
		public void onComplete(Bundle values) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onFacebookError(FacebookError e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onError(Error e) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * Helper class to run Graph requests asynchronously
	 * 
	 * @author Jamison Greeley (atomicrat2552@gmail.com)
	 * 
	 */
	private class FbRunner extends AsyncFacebookRunner {

		public FbRunner(Facebook fb) {
			super(fb);
		}

	}

	private class Auth extends CloudsdaleAsyncAuth {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}
}
