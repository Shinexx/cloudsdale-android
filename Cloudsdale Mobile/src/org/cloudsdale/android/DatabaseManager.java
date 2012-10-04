package org.cloudsdale.android;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;

import org.cloudsdale.android.db.CloudDatabaseHelper;
import org.cloudsdale.android.db.UserDatabaseHelper;
import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.api.User;

import java.sql.SQLException;

/**
 * Database management class. Used to interact with the various databases needed
 * for the application to function and managed from the application context
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com) <br />
 *         Copyright (c) 2012 Cloudsdale.org
 */
public class DatabaseManager {

	// Database Helpers
	private static UserDatabaseHelper	mUserDbHelper	= null;
	private static CloudDatabaseHelper	mCloudDbHelper	= null;

	// Helper management

	/**
	 * Get the user database helper. The helper is cached, and if a cached
	 * helper exists, it's used instead of instantiating a new one
	 * 
	 * @return The user database helper
	 */
	private static UserDatabaseHelper getUserDbHelper() {
		if (mUserDbHelper == null) {
			mUserDbHelper = UserDatabaseHelper.getHelper(Cloudsdale
			        .getContext());
		}
		return mUserDbHelper;
	}

	/**
	 * Get the cloud database helper. The helper is cached, and if a cached
	 * helper exists, it's used instead of instantiating a new one
	 * 
	 * @return The cloud database helper
	 */
	private static CloudDatabaseHelper getCloudDbHelper() {
		if (mCloudDbHelper == null) {
			mCloudDbHelper = CloudDatabaseHelper.getHelper(Cloudsdale
			        .getContext());
		}
		return mCloudDbHelper;
	}

	/**
	 * Release the user database helper's database connection
	 */
	private static void releaseUserDbHelper() {
		mUserDbHelper.close();
	}

	/**
	 * Release the cloud database helper's database connection
	 */
	private static void releaseCloudDbHelper() {
		mCloudDbHelper.close();
	}

	// C _ U _

	/**
	 * Creates or updates a user
	 * 
	 * @param user
	 *            The user to create/update in the database
	 * @return A boolean representing whether or not the user was stored
	 * @throws SQLException
	 */
	public static boolean storeUser(User user) {
		try {
			Dao<User, String> userDao = getUserDbHelper().getUserDao();
			CreateOrUpdateStatus status = userDao.createOrUpdate(user);
			releaseUserDbHelper();
			if (!status.isCreated() && !status.isUpdated()) {
				// TODO rollback the database
			}
			releaseUserDbHelper();
			return (status.isCreated() || status.isUpdated());
		} catch (SQLException e) {
			// TODO rollback the database
			return false;
		}
	}

	/**
	 * Creates or updates a cloud
	 * 
	 * @param cloud
	 *            The cloud to create/update in the
	 * @return A boolean representing whether or not the cloud was stored
	 * @throws SQLException
	 */
	public static boolean storeCloud(Cloud cloud) {
		try {
			Dao<Cloud, String> cloudDao = getCloudDbHelper().getCloudDao();
			CreateOrUpdateStatus status = cloudDao.createOrUpdate(cloud);
			releaseCloudDbHelper();
			if (!status.isCreated() && !status.isUpdated()) {
				// TODO rollback the database
			}
			releaseCloudDbHelper();
			return (status.isCreated() || status.isUpdated());
		} catch (SQLException e) {
			// TODO rollback the database
			return false;
		}
	}

	// _ R _ _

	/**
	 * Reads a user from the database
	 * 
	 * @param id
	 *            The user's id
	 * @return The user in the database or null if it doesn't exist
	 * @throws SQLException
	 */
	public static User readUser(String id) {
		try {
			Dao<User, String> userDao = getUserDbHelper().getUserDao();
			releaseUserDbHelper();
			return userDao.queryForId(id);
		} catch (SQLException e) {
			// TODO rollback the database
			return null;
		}
	}

	/**
	 * Reads a cloud from the database
	 * 
	 * @param id
	 *            The cloud's id
	 * @return The cloud in the database or null if it doesn't exist
	 * @throws SQLException
	 */
	public static Cloud readCloud(String id) {
		try {
			Dao<Cloud, String> cloudDao = getCloudDbHelper().getCloudDao();
			releaseCloudDbHelper();
			return cloudDao.queryForId(id);
		} catch (SQLException e) {
			// TODO rollback the database
			return null;
		}
	}

	// _ _ _ D

	/**
	 * Delete a user in the database
	 * 
	 * @param id
	 *            The id of the user to be deleted
	 * @return A boolean representing whether or not the user was deleted
	 */
	public static boolean deleteUser(String id) {
		try {
			Dao<User, String> userDao = getUserDbHelper().getUserDao();
			int rowsModified = userDao.deleteById(id);
			if (rowsModified != 1) {
				// TODO rollback the database
			}
			releaseUserDbHelper();
			return rowsModified == 1;
		} catch (SQLException e) {
			// TODO rollback the database
			return false;
		}
	}

	/**
	 * Delete a cloud in the database
	 * 
	 * @param id
	 *            The id of the cloud to be deleted
	 * @return A boolean representing whether or not the cloud was deleted
	 */
	public static boolean deleteCloud(String id) {
		try {
			Dao<Cloud, String> cloudDao = getCloudDbHelper().getCloudDao();
			int rowsModified = cloudDao.deleteById(id);
			if (rowsModified != 1) {
				// TODO rollback the database
			}
			releaseCloudDbHelper();
			return rowsModified == 1;
		} catch (SQLException e) {
			// TODO rollback the database
			return false;
		}
	}
}
