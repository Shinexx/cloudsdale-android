package org.cloudsdale.android.managers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.LoggedUser;

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
}
