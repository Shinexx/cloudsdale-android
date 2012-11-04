package org.cloudsdale.android.managers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.api.User;
import org.cloudsdale.android.models.exceptions.QueryException;
import org.cloudsdale.android.models.queries.UserGetQuery;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Manager class designed to store and retrieve Cloudsdale users.
 * Copyright (c) 2012 Cloudsdale.org
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class UserManager {

	private static ArrayList<User>	mStoredUsers;

	static {
		mStoredUsers = new ArrayList<User>();
	}

	/**
	 * Gets the user that has logged into the application
	 * 
	 * @return The user that's logged in
	 */
	public static User getLoggedInUser() {
		Account userAccount = UserAccountManager.getAccount();
		AccountManager am = AccountManager.get(Cloudsdale.getContext());
		String id = am.getUserData(userAccount, UserAccountManager.KEY_ID);
		return getUserById(id);
	}

	/**
	 * Gets a user by ID, fetching from the API as necessary.
	 * 
	 * @param id
	 *            The ID of the user to load
	 * @return The user corresponding to the passed ID
	 */
	public static User getUserById(String id) {
		if (mStoredUsers != null && !mStoredUsers.isEmpty()) {
			ArrayList<User> users;
			synchronized (mStoredUsers) {
				if (mStoredUsers != null) {
					users = new ArrayList<User>(mStoredUsers);
				} else {
					users = null;
				}
			}
			if (users != null && !users.isEmpty()) {
				for (User u : users) {
					if (u.getId().equals(id)) { return u; }
				}
			}
			return null;
		} else {
			// Get the strings we need
			Context appContext = Cloudsdale.getContext();
			String url = appContext.getString(R.string.cloudsdale_api_base)
					+ appContext.getString(R.string.cloudsdale_user_endpoint,
							id);
			String authToken = appContext
					.getString(R.string.cloudsdale_auth_token);

			// Build and execute the query
			UserGetQuery query = new UserGetQuery(url);
			query.addHeader("X-AUTH-TOKEN", authToken);
			try {
				return query.execute(null, null);
			} catch (QueryException e) {
				return null;
			}
		}
	}

	/**
	 * Stores a user
	 * 
	 * @param user
	 *            The user to store
	 */
	public static void storeUser(User user) {
		synchronized (mStoredUsers) {
			mStoredUsers.add(user);
		}
	}
}
