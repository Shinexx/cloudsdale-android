package org.cloudsdale.android.managers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.LoggedUser;
import org.cloudsdale.android.ui.LoginActivity;
import org.cloudsdale.android.ui.StartActivity;

/**
 * Class to manage the user's account object
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 *         Copyright (c) 2012 Cloudsdale.org
 * 
 */
public class UserAccountManager {

	public static final String	KEY_ID				= "id";
	public static final String	PREFERENCES_NAME	= "LoggedUserPrefs";

	private static Account		sUserAccount;

	/**
	 * Store the user's account in the system AccountManager
	 * 
	 * @param user
	 *            The user to store
	 * @return Whether the user account was stored or not
	 */
	public static boolean storeAccount(LoggedUser user) {
		Context appContext = Cloudsdale.getContext();
		Account account = new Account(user.getName(),
				appContext.getString(R.string.account_type));
		final Bundle extras = new Bundle();
		AccountManager am = AccountManager.get(appContext);
		boolean accountCreated = am.addAccountExplicitly(account,
				user.getAuthToken(), extras);
		if (accountCreated) {
			sUserAccount = account;
			SharedPreferences prefs = appContext.getSharedPreferences(
					PREFERENCES_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor prefsEdit = prefs.edit();
			prefsEdit.putString(KEY_ID, user.getId());
			prefsEdit.commit();
		}
		return accountCreated;
	}

	/**
	 * Cache an already existing user account
	 * 
	 * @param account
	 */
	public static void cacheAccount(Account account) {
		sUserAccount = account;
	}

	/**
	 * Get the cached user account
	 * 
	 * @return The stored user account or
	 */
	public static Account getAccount() {
		if (sUserAccount != null) {
			return sUserAccount;
		} else {
			Context context = Cloudsdale.getContext();
			AccountManager am = AccountManager.get(context);
			Account[] accounts = am.getAccountsByType(context
					.getString(R.string.account_type));
			if (accounts.length > 0) {
				Account account = accounts[0];
				cacheAccount(account);
				return account;
			} else {
				return null;
			}
		}
	}

	public static void deleteAccount() {
		if (sUserAccount != null) {
			sUserAccount = null;
		}
		Editor edit = Cloudsdale.getContext()
				.getSharedPreferences(PREFERENCES_NAME, 0).edit();
		edit.remove(KEY_ID);
		edit.commit();
		AccountManager am = AccountManager.get(Cloudsdale.getContext());
		am.removeAccount(sUserAccount, new AccountManagerCallback<Boolean>() {

			@Override
			public void run(AccountManagerFuture<Boolean> arg0) {
				Intent intent = new Intent();
				intent.setClass(Cloudsdale.getContext(), LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Cloudsdale.getContext().startActivity(intent);
			}
		}, null);
	}
}
