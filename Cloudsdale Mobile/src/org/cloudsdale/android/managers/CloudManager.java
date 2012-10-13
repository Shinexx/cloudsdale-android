package org.cloudsdale.android.managers;

import org.cloudsdale.android.models.api.Cloud;

public class CloudManager {
	
	public static Cloud getCloudById(String id) {
		return DatabaseManager.readCloud(id);
	}
	
	public static boolean storeCloud(Cloud cloud) {
		return DatabaseManager.storeCloud(cloud);
	}

}
