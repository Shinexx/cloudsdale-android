package org.cloudsdale.android.managers;

import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.exceptions.QueryException;

import java.util.ArrayList;
import java.util.HashMap;

public class CloudManager extends ManagerBase {

	private HashMap<String, Cloud>	mCachedClouds;

	/**
	 * Stores a cloud in the cache for quick retrieval
	 * 
	 * @param cloud
	 *            The cloud to store
	 */
	public void storeCloud(Cloud cloud) {
		mWriteLock.lock();
		mCachedClouds.put(cloud.getId(), cloud);
		mWriteLock.unlock();
	}

	public Cloud getCloud(String id) throws QueryException {
		// TODO Re-implement using the API client
		return null;
	}

	public ArrayList<Cloud> getCloudsForUser(final String userId)
			throws QueryException {
		// TODO Re-implement using the API client
		return null;
	}
}
