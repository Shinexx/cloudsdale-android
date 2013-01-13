package org.cloudsdale.android.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.bugsense.trace.BugSenseHandler;

import org.cloudsdale.android.R;

public class StartActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// SET THEMES HERE
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.activity_start);
		// Setup BugSense
		BugSenseHandler.initAndStartSession(this, "2bccbeb2");
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Get the account
		Context context = getApplicationContext();
		AccountManager am = AccountManager.get(context);
		Account[] accounts = am.getAccountsByType(context
				.getString(R.string.account_type));

		if (accounts.length > 0) {
			// TODO Act when at least one user is signed in
		} else {
			// TODO Act when no users are signed in
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

}
