package org.cloudsdale.android.ui;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.bugsense.trace.BugSenseHandler;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;

public class StartActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.activity_start);
		BugSenseHandler.initAndStartSession(this, "2bccbeb2");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Account[] accounts = ((Cloudsdale) getApplication())
				.getSessionManager().getAccounts();

		Intent intent = new Intent();
		if (accounts.length > 0) {
			intent.setClass(this, CloudsdaleActivity.class);
		} else {
			intent.setClass(this, LoginActivity.class);
		}
		startActivity(intent);
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

}
