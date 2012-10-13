package org.cloudsdale.android.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.managers.UserAccountManager;

public class StartActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// SET THEMES HERE
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.activity_start);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Get the account
		Context context = Cloudsdale.getContext();
		AccountManager am = AccountManager.get(context);
		Account[] accounts = am.getAccountsByType(context
				.getString(R.string.account_type));

		if (accounts.length > 0) {
			UserAccountManager.cacheAccount(accounts[0]);
			Intent intent = new Intent();
			intent.setClass(this, HomeActivity.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent();
			intent.setClass(this, LoginActivity.class);
			startActivity(intent);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
		
}
