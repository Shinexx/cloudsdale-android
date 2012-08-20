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
import java.util.Collection;
import java.util.HashMap;

/**
 * Persistent Data object for Cloudsdale. Stores items in the flat file system
 * on external memory through basic CRUD operations
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class PersistentData {

    private static final String           TAG = "Cloudsdale PersistentData";
    private static LoggedUser             sLoggedUser;
    private static HashMap<String, Cloud> sCachedClouds;
    private static File                   sUserFile;
    private static File                   sCloudFile;

    public static void initialize(final Context context) {
        if (sCachedClouds == null) {
            sCachedClouds = new HashMap<String, Cloud>();
        }
        if (sUserFile == null) {
            sUserFile = new File(getExternalStorage(context) + "/me");
        }
        if (sCloudFile == null) {
            sCloudFile = new File(PersistentData.getExternalStorage(context)
                    + "/clouds");
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
    public static void deleteCloud(String cloudId) {
        synchronized (sCachedClouds) {
            sCachedClouds.remove(cloudId);
        }

        ArrayList<Cloud> clouds = getClouds();
        storeClouds(clouds);
    }

    /**
     * Delete the logged in user stored on the file system
     * 
     * @param context
     *            The activity context
     */
    public static void deleteMe(Context context) {

        synchronized (sUserFile) {
            // If it's there, delete it
            // LAWL DEFENSIVE CODE, EVEN IF NO ONE WILL EVER TOUCH THIS :D
            if (sUserFile.exists()) {
                sUserFile.delete();
            }
        }

        sLoggedUser = null;
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
    public static Cloud getCloud(String cloudId) {
        boolean cloudCached;

        synchronized (sCachedClouds) {
            cloudCached = sCachedClouds.containsKey(cloudId);
        }

        if (cloudCached) {
            loadClouds();
        }

        synchronized (sCachedClouds) {
            return sCachedClouds.get(cloudId);
        }
    }

    /**
     * Get an array list of all the clouds stored on the file system
     * 
     * @return An ArrayList of all cached clouds
     */
    public static ArrayList<Cloud> getClouds() {
        synchronized (sCachedClouds) {
            return new ArrayList<Cloud>(sCachedClouds.values());
        }
    }

    /**
     * Refreshes the cache with all the clouds on the file system
     */
    public static void loadClouds() {
        new Thread() {

            public void run() {
                try {
                    synchronized (sCloudFile) {
                        if (!sCloudFile.exists()) { return; }

                        // Build the JSON
                        BufferedReader br = new BufferedReader(new FileReader(
                                sCloudFile));
                        String json = br.readLine();
                        br.close();

                        // Deserialize the array
                        Gson gson = new Gson();
                        Cloud[] clouds_temp = gson
                                .fromJson(json, Cloud[].class);

                        // Convert the array to a list
                        if (clouds_temp != null) {
                            for (Cloud c : clouds_temp) {
                                synchronized (sCachedClouds) {
                                    sCachedClouds.put(c.getId(), c);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    BugSenseHandler.log(PersistentData.TAG, e);
                }
            };

        }.start();
    }

    /**
     * Gets the proper external storage location according to the systems API
     * level
     * 
     * @return Null unless the external media is mounted and writable, then a
     *         file representing the directory of the external app storage
     */
    public static File getExternalStorage(final Context context) {
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
     * @return The logged in user
     */
    public static LoggedUser getMe() {
        return PersistentData.sLoggedUser;
    }

    /**
     * Store a cloud on the file system and caches it
     * 
     * @param cloud
     *            The cloud to be stored
     */
    public static void storeCloud(final Cloud cloud) {
        new Thread() {

            public void run() {
                try {
                    synchronized (sCloudFile) {
                        ArrayList<Cloud> clouds;
                        if (!sCloudFile.exists()) {
                            sCloudFile.createNewFile();
                            clouds = new ArrayList<Cloud>();
                        } else {
                            // Get any existing clouds
                            clouds = PersistentData.getClouds();
                        }

                        // Add the cloud
                        clouds.add(cloud);

                        // Serialize and store the cloud object
                        BufferedWriter bw = new BufferedWriter(new FileWriter(
                                sCloudFile));
                        Gson gson = new Gson();
                        bw.write(gson.toJson(clouds.toArray()));
                        bw.flush();
                        bw.close();
                    }

                    synchronized (sCachedClouds) {
                        sCachedClouds.put(cloud.getId(), cloud);
                    }
                } catch (IOException e) {
                    BugSenseHandler.log(PersistentData.TAG, e);
                }
            };

        }.start();

    }

    /**
     * Stores a collection of clouds on the file system and caches them
     * 
     * @param clouds
     *            The clouds to be stored
     */
    public static void storeClouds(final Collection<Cloud> clouds) {
        new Thread() {

            public void run() {
                try {
                    synchronized (sCloudFile) {
                        if (!sCloudFile.exists()) {
                            sCloudFile.createNewFile();
                        }

                        // Get any existing clouds
                        ArrayList<Cloud> existing = PersistentData.getClouds();

                        // If there aren't any existing clouds, create the data
                        // object
                        if (existing == null) {
                            existing = new ArrayList<Cloud>(clouds);
                        } else {
                            existing.addAll(clouds);
                        }

                        // Serialize and store the cloud object
                        Gson gson = new Gson();
                        BufferedWriter bw = new BufferedWriter(new FileWriter(
                                sCloudFile));
                        bw.write(gson.toJson(clouds.toArray()));
                        bw.flush();
                        bw.close();
                    }
                    for (Cloud c : clouds) {
                        synchronized (sCachedClouds) {
                            sCachedClouds.put(c.getId(), c);
                        }
                    }
                } catch (IOException e) {
                    BugSenseHandler.log(PersistentData.TAG, e);
                }
            };

        }.start();
    }

    /**
     * Store the logged in user to the external flat file system
     * 
     * @param me
     *            The logged in user
     */
    public static void storeLoggedUser(final LoggedUser me) {
        new Thread() {

            @Override
            public void run() {
                super.run();
                try {
                    synchronized (sUserFile) {
                        if (!sUserFile.exists()) {
                            sUserFile.createNewFile();
                        }

                        // Serialize and store the user
                        Gson gson = new Gson();
                        BufferedWriter bw = new BufferedWriter(new FileWriter(
                                sUserFile));
                        bw.write(gson.toJson(me));
                        bw.flush();
                        bw.close();
                    }

                    PersistentData.sLoggedUser = me;
                    storeClouds(me.getClouds());
                } catch (IOException e) {
                    BugSenseHandler.log(PersistentData.TAG, e);
                }
            }
        }.start();
    }

}
