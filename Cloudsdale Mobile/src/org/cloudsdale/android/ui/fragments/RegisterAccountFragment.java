package org.cloudsdale.android.ui.fragments;

import org.cloudsdale.android.R;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class RegisterAccountFragment extends SherlockFragment {

	private EditText	emailField;
	private EditText	passwordField;
	private EditText	screennameField;

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
				R.layout.login_view_registration, container, false);

		Button registerButton = (Button) layout
				.findViewById(R.id.loginViewRegistrationSubmitButton);
		emailField = (EditText) layout
				.findViewById(R.id.registrationViewEmailField);
		passwordField = (EditText) layout
				.findViewById(R.id.registrationViewPasswordField);
		screennameField = (EditText) layout
				.findViewById(R.id.registrationViewScreennameField);
		
		// Logic to start CD Registration flow
		registerButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String email = emailField.getText().toString();
				String password = passwordField.getText().toString();
				String screenName = screennameField.getText().toString();

				// TODO Implement registration logic
				
				Toast.makeText(v.getContext(), "Registration not availible",
						Toast.LENGTH_LONG).show();
			}
		});

		// Set the gravity of the layout
		layout.setGravity(Gravity.CENTER);
		
		return layout;
	}

}
