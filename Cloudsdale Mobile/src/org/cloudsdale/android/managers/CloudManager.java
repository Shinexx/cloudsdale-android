package org.cloudsdale.android.managers;

import android.os.AsyncTask;

import org.cloudsdale.android.models.api.Cloud;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CloudManager {
	
	private static ArrayList<Cloud> mStoredClouds;
	
	static {
		mStoredClouds = new ArrayList<Cloud>();
	}
	
	public static Cloud getCloudById(String id) {
		if(mStoredClouds != null && !mStoredClouds.isEmpty()) {
			CloudLoadTask task = new CloudLoadTask();
			task.execute(id);
			try {
				return task.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return null;
			} catch (ExecutionException e) {
				e.printStackTrace();
				return null;
			}
		} else return null;
	}
	
	public static void storeCloud(Cloud cloud) {
		synchronized (mStoredClouds) {
			mStoredClouds.add(cloud);
		}
	}
	
	static class CloudLoadTask extends AsyncTask<String, Void, Cloud> {
		@Override
		protected Cloud doInBackground(String... params) {
			ArrayList<Cloud> clouds;
			synchronized (mStoredClouds) {
				clouds = new ArrayList<Cloud>(mStoredClouds);
			}
			for(Cloud c : clouds) {
				if(c.getId().equals(params[0])) {
					return c;
				}
			}
			return null;
		}
	}

}
