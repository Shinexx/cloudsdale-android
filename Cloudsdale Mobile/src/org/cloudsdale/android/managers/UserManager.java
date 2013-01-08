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
public class UserManager extends ManagerBase {

	private HashMap<String, User>	mStoredUsers;

	public UserManager() {
		super("UserManagerThread");
		mStoredUsers = new HashMap<String, User>();
	}

	/**
	 * Gets the user that has logged into the application
	 * 
	 * @return The user that's logged in
	 * @throws QueryException
	 *             When the query cannot be completed
	 */
	public User getLoggedInUser() throws QueryException {
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
	public User getUserById(final String id) throws QueryException {
		val userIsStored = mStoredUsers.containsKey(id);
		if (userIsStored) {
			mReadLock.lock();
			val user =  mStoredUsers.get(id);
			mReadLock.unlock();
			return user;
		} else {
			// Get the strings we need
			Context appContext = Cloudsdale.getContext();
			final String url = appContext
					.getString(R.string.cloudsdale_api_base)
					+ appContext.getString(R.string.cloudsdale_user_endpoint,
							id);

			mNetworkHandler.post(new Runnable() {

				@Override
				public void run() {
					// Build and execute the query
					UserGetQuery query = new UserGetQuery(url);
					User result;
					try {
						result = query.execute(null, null);
						if (result != null) {
							storeUser(result);
						}
					} catch (QueryException e) {
						// Don't worry here, the method body will catch it
					}
					synchronized (UserManager.this) {
						UserManager.this.notify();
					}
				}
			});

			try {
				synchronized (this) {
					wait();
				}
				if (mStoredUsers.containsKey(id)) {
					return mStoredUsers.get(id);
				} else {
					throw new QueryException("Thread failed", 418);
				}
			} catch (InterruptedException e) {
				throw new QueryException("Thread was interrupted", 418);
			}
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
		final ArrayList<Cloud> clouds = new ArrayList<Cloud>(
				Arrays.asList(query.executeForCollection(null, null)));
		user.setClouds(clouds);
		new Thread() {
			public void run() {
				for (Cloud cloud : clouds) {
					Cloudsdale.getNearestPegasus().storeCloud(cloud);
				}
			};
		}.start();
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
