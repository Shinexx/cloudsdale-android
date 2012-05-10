package org.cloudsdale.android.ui;

import org.cloudsdale.android.R;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
		
		// Set the layout gravity
		layout.setGravity(Gravity.CENTER);

		return layout;
	}
}
