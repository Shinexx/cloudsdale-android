package org.cloudsdale.android.managers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

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
public class SessionManager extends ManagerBase {

	public static final String	KEY_ID	= "id";

	private Account				mActiveSession;
	private AccountManager		mAccountManager;

	public SessionManager(Cloudsdale cloudsdale) {
		super(cloudsdale);
	}

	public AccountManager getAccountManager() {
		if (mAccountManager == null) {
			mAccountManager = AccountManager.get(mAppInstance.getContext());
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
		Context appContext = mAppInstance.getContext();
		User user = session.getUser();
		Account account = new Account(user.getName(),
				appContext.getString(R.string.account_type));
		AccountManager am = getAccountManager();
		boolean accountCreated = am.addAccountExplicitly(account,
				user.getAuthToken(), null);
		if (accountCreated) {
			am.setUserData(account, KEY_ID, session.getUser().getId());
			mActiveSession = account;
		}
		return accountCreated;
	}

	/**
	 * Sets the account associated with the active session
	 * 
	 * @param account
	 *            The account to set as active
	 */
	public void setActiveSession(Account account) {
		mActiveSession = account;
	}

	/**
	 * Gets the account associated with the currently active session
	 * 
	 * @return The account associated with the active session
	 */
	public Account getActiveSession() {
		return mActiveSession;
	}

	/**
	 * Get the cached user account
	 * 
	 * @return The stored user account or
	 */
	public Account[] getAccounts() {
		Context context = mAppInstance.getContext();
		AccountManager am = getAccountManager();
		Account[] accounts = am.getAccountsByType(context
				.getString(R.string.account_type));
		return accounts;
	}

	public void deleteAccount() {
		// TODO Be able to delete accounts
	}
}
