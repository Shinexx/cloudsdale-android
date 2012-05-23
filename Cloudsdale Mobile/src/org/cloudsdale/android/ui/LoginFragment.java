package org.cloudsdale.android.ui;

import org.cloudsdale.android.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

/**
 * Code behind for the Login fragment. Supports FB and Twitter login
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class LoginFragment extends SherlockFragment {

	private final String	TAG	= "Cloudsdale LoginFragment";
	public static Facebook	fb;

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
	private void startFacebookAuthFlow() {
		// Create the FB instance as needed
		if (fb == null) {
			fb = new Facebook(getString(R.string.facebook_api_token));
		}

		// Start the auth flow
		fb.authorize(LoginFragment.this.getActivity(), new String[] {},
				LoginViewActivity.FACEBOOK_ACTIVITY_CODE, new FbListener());
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
			// TODO Handle the actual auth flow
			Log.d(TAG, "Facebook auth completed");
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
}
