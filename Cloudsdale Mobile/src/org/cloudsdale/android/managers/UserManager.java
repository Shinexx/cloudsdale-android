package org.cloudsdale.android.managers;

import android.accounts.Account;
import android.accounts.AccountManager;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.models.api.User;
import org.cloudsdale.android.models.exceptions.QueryException;

import java.util.HashMap;

/**
 * Manager class designed to cache and retrieve Cloudsdale users. <br />
 * Copyright (c) 2012 Cloudsdale.org
 * 
 * @author Jamison Greeley (berwyn.codeweaver@gmail.com)
 * 
 */
public class UserManager extends ManagerBase {

	private HashMap<String, User>	mCachedUsers;

	public UserManager(Cloudsdale mAppInstance) {
		super(mAppInstance);
		mCachedUsers = new HashMap<String, User>();
	}

	public String getLoggedInUserId() {
		Account activeAccount = mAppInstance.getSessionManager()
				.getActiveSession();
		AccountManager am = mAppInstance.getSessionManager()
				.getAccountManager();
		String id = am.getUserData(activeAccount, SessionManager.KEY_ID);
		return id;
	}

	/**
	 * Gets the user that has logged into the application
	 * 
	 * @return The user that's logged in
	 * @throws QueryException
	 *             When the query cannot be completed
	 */
	public User getLoggedInUser() {
		Account activeAccount = mAppInstance.getSessionManager()
				.getActiveSession();
		if (activeAccount != null) {
			AccountManager am = mAppInstance.getSessionManager()
					.getAccountManager();
			String id = am.getUserData(activeAccount, SessionManager.KEY_ID);
			User loggedInUser = getUserById(id);
			return loggedInUser;
		} else {
			return null;
		}
	}

	/**
	 * Gets a user by ID, fetching from the API as necessary.
	 * 
	 * @param id
	 *            The ID of the user to load
	 * @return The user corresponding to the passed ID or null if they aren't
	 *         cached
	 */
	public User getUserById(String id) {
		if (mCachedUsers.containsKey(id)) {
			mReadLock.lock();
			User returnUser = mCachedUsers.get(id);
			mReadLock.unlock();
			return returnUser;
		} else {
			return null;
		}
	}

	/**
	 * Stores a user
	 * 
	 * @param user
	 *            The user to store
	 */
	public void storeUser(User user) {
		mWriteLock.lock();
		mCachedUsers.put(user.getId(), user);
		mWriteLock.unlock();
	}
}
