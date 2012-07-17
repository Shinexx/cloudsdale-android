package org.cloudsdale.android;

import android.content.Context;
import android.os.Environment;

import com.bugsense.trace.BugSenseHandler;
import com.google.gson.Gson;

import org.cloudsdale.android.models.LoggedUser;
import org.cloudsdale.android.models.api_models.Cloud;
import org.cloudsdale.android.models.api_models.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Persistent Data object for Cloudsdale. Stores items in the flat file system
 * on external memory through basic CRUD operations
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class PersistentData {

	private static final String	TAG	= "Cloudsdale PersistentData";
	private static LoggedUser	me;

	/**
	 * Deletes a cloud out of the stored list
	 * 
	 * @param context
	 *            The activity context
	 * @param cloud
	 *            The cloud to be deleted
	 */
	public static void deleteCloud(Context context, Cloud cloud) {
		// Get all the clouds
		ArrayList<Cloud> clouds = PersistentData.getClouds(context);

		// If the cloud is in the list, delete it
		if (clouds != null) {
			if (clouds.contains(cloud)) {
				clouds.remove(cloud);
			}
		}
	}

	/**
	 * Delete a cloud from the stored list
	 * 
	 * @param context
	 *            The activity context
	 * @param cloudId
	 *            The string ID of the cloud to be deleted
	 */
	public static void deleteCloud(Context context, String cloudId) {
		// Get all the clouds
		ArrayList<Cloud> clouds = PersistentData.getClouds(context);

		// Get the cloud
		Cloud cloud = PersistentData.getCloud(context, cloudId);

		// If the cloud is in the list, delete it
		if (clouds != null && cloud != null && clouds.contains(cloud)) {
			clouds.remove(cloud);
		}
	}

	/**
	 * Delete the logged in user stored on the file system
	 * 
	 * @param context
	 *            The activity context
	 */
	public static void deleteMe(Context context) {
		// Get the me file
		File external = new File(PersistentData.getExternalStorage(context)
				+ "/me");

		// If it's there, delete it
		// LAWL DEFENSIVE CODE, EVEN IF NO ONE WILL EVER TOUCH THIS :D
		if (external.exists()) {
			external.delete();
		}

		me = null;
	}

	/**
	 * Get a specific cloud off of the file system
	 * 
	 * @param cloudId
	 *            The string ID of the cloud to search for
	 * @param context
	 *            The activity context
	 * @return The cloud with the passed ID
	 */
	public static Cloud getCloud(Context context, String cloudId) {
		Cloud cloud = null;
		File external;

		try {
			// Get the external file
			external = new File(PersistentData.getExternalStorage(context)
					+ "/clouds");
			if (!external.exists()) { return cloud; }

			// Build the JSON
			Gson gson = new Gson();
			BufferedReader br = new BufferedReader(new FileReader(external));
			String json = br.readLine();
			br.close();

			// Deserialize the array
			Cloud[] clouds_temp = gson.fromJson(json, Cloud[].class);

			// Get the cloud by id
			if (clouds_temp != null) {
				for (Cloud _cloud : clouds_temp) {
					if (_cloud.getId().equals(cloudId)) {
						cloud = _cloud;
						break;
					}
				}
			}
		} catch (IOException e) {
			BugSenseHandler.log(PersistentData.TAG, e);
		}

		return cloud;
	}

	/**
	 * Get an array list of all the clouds stored on the file system
	 * 
	 * @param context
	 *            The context of the activity
	 * @return An ArrayList of all clouds on the file system
	 */
	public static ArrayList<Cloud> getClouds(Context context) {
		ArrayList<Cloud> clouds = null;
		File external;

		try {
			// Get the external file
			external = new File(PersistentData.getExternalStorage(context)
					+ "/clouds");
			if (!external.exists()) { return clouds; }

			// Build the JSON
			Gson gson = new Gson();
			BufferedReader br = new BufferedReader(new FileReader(external));
			String json = br.readLine();
			br.close();

			// Deserialize the array
			Cloud[] clouds_temp = gson.fromJson(json, Cloud[].class);

			// Convert the array to a list
			if (clouds_temp != null) {
				clouds = new ArrayList<Cloud>(Arrays.asList(clouds_temp));
			} else {
				User me = PersistentData.getMe(context);
				clouds = new ArrayList<Cloud>(me.getClouds());
			}
		} catch (IOException e) {
			BugSenseHandler.log(PersistentData.TAG, e);
		}

		return clouds;
	}

	/**
	 * Gets the proper external storage location according to the systems API
	 * level
	 * 
	 * @return Null unless the external media is mounted and writable, then a
	 *         file representing the directory of the external app storage
	 */
	public static File getExternalStorage(Context context) {
		File externalDir = null;
		boolean externalAvailible;
		boolean externalWriteable;

		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// External is mounted and writeable
			externalAvailible = externalWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// External is mounted but not writeable
			externalAvailible = true;
			externalWriteable = false;
		} else {
			// External isn't mounted at all
			externalAvailible = externalWriteable = false;
		}

		if (externalAvailible && externalWriteable) {
			externalDir = context.getExternalFilesDir(null);
		}

		// Create the app folder if it doesn't exist
		if (!externalDir.exists()) {
			externalDir.mkdir();
		}

		return externalDir;
	}

	/**
	 * Get the logged in user off of the file system
	 * 
	 * @param context
	 *            The activity context
	 * @return The logged in user
	 */
	public static LoggedUser getMe(Context context) {
		File external;

		if (me == null) {
			try {
				external = new File(PersistentData.getExternalStorage(context)
						+ "/me");
				if (!external.exists()) { return null; }

				// Deserialize the user
				Gson gson = new Gson();
				BufferedReader br = new BufferedReader(new FileReader(external));
				String json = br.readLine();
				br.close();

				me = gson.fromJson(json, LoggedUser.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return PersistentData.me;
	}

	/**
	 * Store a cloud on the file system
	 * 
	 * @param cloud
	 *            The cloud to be stored
	 * @param context
	 *            The activity context
	 */
	public static void storeCloud(Context context, Cloud cloud) {
		try {
			// Get the external file
			File external = new File(PersistentData.getExternalStorage(context)
					+ "/clouds");
			if (!external.exists()) {
				external.createNewFile();
			}

			// Get any existing clouds
			ArrayList<Cloud> clouds = PersistentData.getClouds(context);

			// If there aren't any existing clouds, create the data object
			if (clouds == null) {
				clouds = new ArrayList<Cloud>();
			}

			// Add the cloud
			clouds.add(cloud);

			// Serialize and store the cloud object
			Gson gson = new Gson();
			BufferedWriter bw = new BufferedWriter(new FileWriter(external));
			bw.write(gson.toJson(clouds.toArray()));
			bw.flush();
			bw.close();
		} catch (IOException e) {
			BugSenseHandler.log(PersistentData.TAG, e);
		}
	}

	/**
	 * Store the logged in user to the external flat file system
	 * 
	 * @param me
	 *            The logged in user
	 */
	public static void StoreMe(Context context, LoggedUser me) {
		try {
			File external = new File(PersistentData.getExternalStorage(context)
					+ "/me");
			if (!external.exists()) {
				external.createNewFile();
			}

			// Serialize and store the user
			Gson gson = new Gson();
			BufferedWriter bw = new BufferedWriter(new FileWriter(external));
			bw.write(gson.toJson(me));
			bw.flush();
			bw.close();
			
			PersistentData.me = me;
		} catch (IOException e) {
			BugSenseHandler.log(PersistentData.TAG, e);
		}
	}

}
