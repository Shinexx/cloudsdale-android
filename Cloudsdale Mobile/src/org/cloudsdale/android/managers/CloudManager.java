package org.cloudsdale.android.managers;

import android.content.Context;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.exceptions.QueryException;
import org.cloudsdale.android.models.queries.CloudGetQuery;

import java.util.HashMap;

import lombok.val;

public class CloudManager extends ManagerBase {

	/**
	 * No argument constructor
	 */
	public CloudManager() {
		super("CloudManagerThread");
		mCachedClouds = new HashMap<String, Cloud>();
	}

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
		val isCached = mCachedClouds.containsKey(id);
		if (isCached) {
			mReadLock.lock();
			val cloud = mCachedClouds.get(id);
			mReadLock.unlock();
			return cloud;
		} else {
			Context appContext = Cloudsdale.getContext();
			final String url = appContext
					.getString(R.string.cloudsdale_api_base)
					+ appContext.getString(R.string.cloudsdale_cloud_endpoint,
							id);

			// Build and execute the query
			mNetworkHandler.post(new Runnable() {

				@Override
				public void run() {
					CloudGetQuery query = new CloudGetQuery(url);
					Cloud result;
					try {
						result = query.execute(null, null);
						if (result != null) storeCloud(result);
					} catch (QueryException e) {
						// Don't worry about it, method will catch the failure
					}
					CloudManager.this.notify();
				}
			});

			try {
				wait();
				if (mCachedClouds.containsKey(id)) {
					return mCachedClouds.get(id);
				} else {
					throw new QueryException("Thread failed", 418);
				}
			} catch (InterruptedException e) {
				throw new QueryException("Thread was interrupted", 418);
			}

		}

	}

}
