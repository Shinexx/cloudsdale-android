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

/**
 * Manager class designed to store and retrieve Cloudsdale users. Copyright (c)
 * 2012 Cloudsdale.org
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class UserManager {

	private static HashMap<String, User>	mStoredUsers;

	static {
		mStoredUsers = new HashMap<String, User>();
	}

	/**
	 * Gets the user that has logged into the application
	 * 
	 * @return The user that's logged in
	 */
	public synchronized static User getLoggedInUser() {
		String id = Cloudsdale
				.getContext()
				.getSharedPreferences(UserAccountManager.PREFERENCES_NAME,
						Context.MODE_PRIVATE)
				.getString(UserAccountManager.KEY_ID, "");
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
	 */
	public static User getUserById(String id) {
		if (mStoredUsers.containsKey(id)) {
			return mStoredUsers.get(id);
		} else {
			// Get the strings we need
			Context appContext = Cloudsdale.getContext();
			String url = appContext.getString(R.string.cloudsdale_api_base)
					+ appContext.getString(R.string.cloudsdale_user_endpoint,
							id);

			// Build and execute the query
			UserGetQuery query = new UserGetQuery(url);
			try {
				User user = query.execute(null, null);
				mStoredUsers.put(id, user);
				return user;
			} catch (QueryException e) {
				return null;
			}
		}
	}

	/**
	 * Gets the clouds for a user from the API
	 * 
	 * @param user
	 *            The user to fetch clouds for
	 */
	public static void getCloudsForUser(User user) {
		Context appContext = Cloudsdale.getContext();
		String url = appContext.getString(R.string.cloudsdale_api_base)
				+ appContext.getString(
						R.string.cloudsdale_user_clouds_endpoint, user.getId());
		CloudGetQuery query = new CloudGetQuery(url);
		try {
			ArrayList<Cloud> clouds = new ArrayList<Cloud>(Arrays.asList(query
					.executeForCollection(null, null)));
			user.convertClouds(clouds);
		} catch (QueryException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get a cloud from a user by ID
	 * 
	 * @param user
	 *            The user to get the cloud from
	 * @param id
	 *            The ID of the cloud to get
	 * @return The cloud if the user is a member of it, null otherwise
	 */
	public static Cloud getCloudFromUser(User user, String id) {
		return user.getCloud(id);
	}

	/**
	 * Stores a user
	 * 
	 * @param user
	 *            The user to store
	 */
	public static void storeUser(User user) {
		synchronized (mStoredUsers) {
			mStoredUsers.put(user.getId(), user);
		}
	}
}
