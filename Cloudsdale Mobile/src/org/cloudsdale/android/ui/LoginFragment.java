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

public class LoginFragment extends SherlockFragment {

	private final String	TAG	= "Cloudsdale LoginFragment";
	public static Facebook	fb;

	static RegisterAccountFragment newInstance() {
		return new RegisterAccountFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Bind the data objects
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.login_view_option_buttons, container, false);

		// Create the button listeners
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

	private void startFacebookAuthFlow() {
		Facebook fb = new Facebook(getString(R.string.facebook_api_token));
		fb.authorize(LoginFragment.this.getActivity(), new String[] {},
				LoginViewActivity.FACEBOOK_ACTIVITY_CODE, new DialogListener() {

					@Override
					public void onComplete(Bundle values) {
						Log.v(TAG, "Facebook auth completed");
					}

					@Override
					public void onFacebookError(FacebookError e) {
						Toast.makeText(
								LoginFragment.this.getActivity(),
								"There was an error with Facebook, please try again",
								Toast.LENGTH_LONG).show();
						Log.e(TAG, "Facebook Error: " + e.getMessage());
					}

					@Override
					public void onError(DialogError e) {
						Toast.makeText(LoginFragment.this.getActivity(),
								"There was an error, please try again",
								Toast.LENGTH_LONG).show();
						Log.e(TAG,
								"Error during Facebook login: "
										+ e.getMessage());
					}

					@Override
					public void onCancel() {
						// Do nothing, already on login screen
					}

				});
	}

}
