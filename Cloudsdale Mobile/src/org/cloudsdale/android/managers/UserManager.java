package org.cloudsdale.android.managers;

import android.content.Context;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.api.User;
import org.cloudsdale.android.models.exceptions.QueryException;
import org.cloudsdale.android.models.queries.CloudGetQuery;
import org.cloudsdale.android.models.queries.UserGetQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import lombok.val;

/**
 * Manager class designed to store and retrieve Cloudsdale users. Copyright (c)
 * 2012 Cloudsdale.org
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class UserManager {

	private HashMap<String, User>	mStoredUsers;

	public UserManager() {
		mStoredUsers = new HashMap<String, User>();
	}

	/**
	 * Gets the user that has logged into the application
	 * 
	 * @return The user that's logged in
	 * @throws QueryException
	 *             When the query cannot be completed
	 */
	public synchronized User getLoggedInUser() throws QueryException {
		val accountManager = Cloudsdale.getUserAccountManager();
		val am = accountManager.getAccountManager();
		val userAccount = accountManager.getAccount();
		val id = am.getUserData(userAccount, UserAccountManager.KEY_ID);
		User user = getUserById(id);
		if (user.getClouds() == null || user.getClouds().isEmpty()) {
			getCloudsForUser(user);
		}
		return user;
	}

	/**
	 * Gets a user by ID, fetching from the API as necessary.
	 * 
	 * @param id
	 *            The ID of the user to load
	 * @return The user corresponding to the passed ID
	 * @throws QueryException
	 *             When the query cannot be completed
	 */
	public synchronized User getUserById(final String id) throws QueryException {
		val userIsStored = mStoredUsers.containsKey(id);
		if (userIsStored) {
			synchronized (mStoredUsers) {
				return mStoredUsers.get(id);
			}
		} else {
			// Get the strings we need
			Context appContext = Cloudsdale.getContext();
			String url = appContext.getString(R.string.cloudsdale_api_base)
					+ appContext.getString(R.string.cloudsdale_user_endpoint,
							id);

			// Build and execute the query
			UserGetQuery query = new UserGetQuery(url);
			User result = query.execute(null, null);
			if (result != null) {
				storeUser(result);
			}
			return result;
		}
	}

	/**
	 * Gets the clouds for a user from the API
	 * 
	 * @param user
	 *            The user to fetch clouds for
	 * @throws QueryException
	 *             When the query can't be completed
	 */
	private void getCloudsForUser(User user) throws QueryException {
		Context appContext = Cloudsdale.getContext();
		String url = appContext.getString(R.string.cloudsdale_api_base)
				+ appContext.getString(
						R.string.cloudsdale_user_clouds_endpoint, user.getId());
		CloudGetQuery query = new CloudGetQuery(url);
		ArrayList<Cloud> clouds = new ArrayList<Cloud>(Arrays.asList(query
				.executeForCollection(null, null)));
		user.setClouds(clouds);
	}

	/**
	 * Stores a user
	 * 
	 * @param user
	 *            The user to store
	 */
	public void storeUser(User user) {
		synchronized (mStoredUsers) {
			mStoredUsers.put(user.getId(), user);
		}
	}
}
