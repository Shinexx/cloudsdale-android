package org.cloudsdale.android.managers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.api.Session;
import org.cloudsdale.android.models.api.User;

/**
 * Class to manage the user's account object
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com) Copyright (c) 2012
 *         Cloudsdale.org
 * 
 */
public class UserAccountManager {

	public static final String	KEY_ID				= "id";
	public static final String	PREFERENCES_NAME	= "LoggedUserPrefs";

	private Account				mUserAccount;
	private AccountManager		mAccountManager;

	public AccountManager getAccountManager() {
		if (mAccountManager == null) {
			mAccountManager = AccountManager.get(Cloudsdale.getContext());
		}
		return mAccountManager;
	}

	/**
	 * Store the user's account in the system AccountManager
	 * 
	 * @param user
	 *            The user to store
	 * @return Whether the user account was stored or not
	 */
	public boolean storeAccount(Session session) {
		Context appContext = Cloudsdale.getContext();
		User user = session.getUser();
		Account account = new Account(user.getName(),
				appContext.getString(R.string.account_type));
		final Bundle extras = new Bundle();
		extras.putString(KEY_ID, user.getId());
		AccountManager am = getAccountManager();
		boolean accountCreated = am.addAccountExplicitly(account,
				user.getAuthToken(), extras);
		if (accountCreated) {
			mUserAccount = account;
		}
		return accountCreated;
	}

	/**
	 * Cache an already existing user account
	 * 
	 * @param account
	 */
	public void cacheAccount(Account account) {
		mUserAccount = account;
	}

	/**
	 * Get the cached user account
	 * 
	 * @return The stored user account or
	 */
	public Account getAccount() {
		if (mUserAccount != null) {
			return mUserAccount;
		} else {
			Context context = Cloudsdale.getContext();
			AccountManager am = getAccountManager();
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

	public void deleteAccount() {
		if (mUserAccount != null) {
			mUserAccount = null;
		}
		Editor edit = Cloudsdale.getContext()
				.getSharedPreferences(PREFERENCES_NAME, 0).edit();
		edit.remove(KEY_ID);
		edit.commit();
		AccountManager am = getAccountManager();
		am.removeAccount(mUserAccount, new AccountManagerCallback<Boolean>() {

			@Override
			public void run(AccountManagerFuture<Boolean> arg0) {
				Intent intent = new Intent();
				// TODO Re-do this once the views are back in place
//				intent.setClass(Cloudsdale.getContext(), LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Cloudsdale.getContext().startActivity(intent);
			}
		}, null);
	}
}
