package org.cloudsdale.android.ui;

import org.cloudsdale.android.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;

public class LoginFragment extends SherlockFragment {

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

		// Set the layout gravity
		layout.setGravity(Gravity.CENTER);

		return layout;
	}
}
