package org.cloudsdale.android;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.cloudsdale.android.models.User;

import android.os.Environment;

import com.google.gson.Gson;

/**
 * Persistent Data object for Cloudsdale
 * 
 * Stores items in the flat file system on external memory, writes to the DB on
 * app death
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class PersistentData {
	
	/**
	 * Gets the proper external storage location according to the systems API
	 * level
	 * 
	 * @return Null unless the external media is mounted and writable, then a
	 *         file representing the directory of the external app storage
	 */
	public static File getExternalStorage() {
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
			if (android.os.Build.VERSION.SDK_INT >= 8) {
				externalDir = Cloudsdale.getContext().getExternalFilesDir(null);
			} else {
				externalDir = Environment.getExternalStorageDirectory();
			}
		}

		return externalDir;
	}

	/**
	 * Store the logged in user to the external flat file system
	 * @param me The logged in user
	 */
	public static void StoreMe(User me) {
		try {
			File external = new File(getExternalStorage().getCanonicalPath() + "me");
			Gson gson = new Gson();
			BufferedWriter bw = new BufferedWriter(new FileWriter(external));
			bw.write(gson.toJson(me));
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static User getMe() {
		User me = null;
		File external;
		
		try {
			external = new File(getExternalStorage() + "me");
			if(!external.exists()) { return me; }
			Gson gson = new Gson();
			BufferedReader br = new BufferedReader(new FileReader(external));
			String json = br.readLine();
			br.close();
			
			me = gson.fromJson(json, User.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return me;
	}
}
