package org.cloudsdale.android.managers;

import android.os.AsyncTask;

import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.api.User;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Database management class. Used to interact with the various databases needed
 * for the application to function and managed from the application context
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com) <br />
 *         Copyright (c) 2012 Cloudsdale.org
 */
public class DatabaseManager {

	// C _ U _

	/**
	 * Creates or updates a user
	 * 
	 * @param user
	 *            The user to create/update in the database
	 * @return
	 */
	public static boolean storeUser(final User user) {
		UserSaveTask task = new UserSaveTask();
		task.execute(user);
		try {
			return task.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Creates or updates a cloud
	 * 
	 * @param cloud
	 *            The cloud to create/update in the
	 * @return
	 */
	public static boolean storeCloud(final Cloud cloud) {
		CloudSaveTask task = new CloudSaveTask();
		task.execute(cloud);
		try {
			return task.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Convenience method to store a collection of clouds stored in a list
	 * 
	 * @param clouds
	 *            A list of Clouds to store
	 * @return 
	 */
	public static boolean storeCloudList(final List<Cloud> clouds) {
		CloudSaveTask task = new CloudSaveTask();
		task.execute((Cloud[]) clouds.toArray());
		try {
			return task.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return false;
	}

	// _ R _ _

	/**
	 * Reads a user from the database
	 * 
	 * @param id
	 *            The user's id
	 * @return The user in the database or null if it doesn't exist
	 */
	public static User readUser(String id) {
		UserReadTask task = new UserReadTask();
		task.execute(id);
		try {
			return task.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Reads a cloud from the database
	 * 
	 * @param id
	 *            The cloud's id
	 * @return The cloud in the database or null if it doesn't exist
	 */
	public static Cloud readCloud(String id) {
		CloudReadTask task = new CloudReadTask();
		task.execute(id);
		try {
			return task.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	// _ _ _ D

	/**
	 * Delete a user in the database
	 * 
	 * @param id
	 *            The id of the user to be deleted
	 */
	public static void deleteUser(final String id) {
		new Thread() {
			@Override
			public void run() {
				User user = readUser(id);
				user.delete();
				super.run();
			}
		}.start();
	}

	/**
	 * Delete a cloud in the database
	 * 
	 * @param id
	 *            The id of the cloud to be deleted
	 */
	public static void deleteCloud(final String id) {
		new Thread() {
			@Override
			public void run() {
				Cloud cloud = readCloud(id);
				cloud.delete();
				super.run();
			}
		}.start();
	}

	static class UserSaveTask extends AsyncTask<User, Void, Boolean> {

		@Override
		protected Boolean doInBackground(User... params) {
			for (User u : params) {
				u.save();
			}
			return true;
		}

	}

	static class UserReadTask extends AsyncTask<String, Void, User> {

		@Override
		protected User doInBackground(String... params) {
			return User.find(User.class, "stringId=?",
					new String[] { params[0] }).get(0);
		}

	}

	static class CloudSaveTask extends AsyncTask<Cloud, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Cloud... params) {
			for (Cloud c : params) {
				c.save();
			}
			return true;
		}

	}

	static class CloudReadTask extends AsyncTask<String, Void, Cloud> {

		@Override
		protected Cloud doInBackground(String... params) {
			return Cloud.find(Cloud.class, "stringId=?",
					new String[] { params[0] }).get(0);
		}

	}
}
