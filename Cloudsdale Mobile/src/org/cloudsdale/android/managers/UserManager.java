package org.cloudsdale.android.managers;

import android.content.Context;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.api.User;
import org.cloudsdale.android.models.exceptions.QueryException;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.val;

/**
 * Manager class designed to store and retrieve Cloudsdale users. Copyright (c)
 * 2012 Cloudsdale.org
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class UserManager extends ManagerBase {

	private HashMap<String, User>	mStoredUsers;

	/**
	 * Gets the user that has logged into the application
	 * 
	 * @return The user that's logged in
	 * @throws QueryException
	 *             When the query cannot be completed
	 */
	public User getLoggedInUser() throws QueryException {
		// TODO Re-implement using API client
		return null;
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
	public User getUserById(final String id) throws QueryException {
		// TODO Re-implement using API client
		return null;
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
		// TODO Re-implement using API client
	}

	/**
	 * Stores a user
	 * 
	 * @param user
	 *            The user to store
	 */
	public void storeUser(User user) {
		mWriteLock.lock();
		mStoredUsers.put(user.getId(), user);
		mWriteLock.unlock();
	}
}
