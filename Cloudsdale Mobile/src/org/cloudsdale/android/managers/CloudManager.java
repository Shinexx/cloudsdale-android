package org.cloudsdale.android.managers;

import android.content.Context;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.exceptions.QueryException;
import org.cloudsdale.android.models.queries.CloudGetQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
					mReadLock.lock();
					val cloud = mCachedClouds.get(id);
					mReadLock.lock();
					return cloud;
				} else {
					throw new QueryException("Thread failed", 418);
				}
			} catch (InterruptedException e) {
				throw new QueryException("Thread was interrupted", 418);
			}

		}

	}

	public ArrayList<Cloud> getCloudsForUser(final String userId)
			throws QueryException {
		final ArrayList<Cloud> clouds = new ArrayList<Cloud>();
		mNetworkHandler.post(new Runnable() {

			@Override
			public void run() {
				Context appContext = Cloudsdale.getContext();
				String url = appContext.getString(R.string.cloudsdale_api_base)
						+ appContext.getString(
								R.string.cloudsdale_user_clouds_endpoint,
								userId);
				CloudGetQuery query = new CloudGetQuery(url);
				try {
					List<Cloud> result = Arrays.asList(query
							.executeForCollection(null, null));
					for (Cloud c : result) {
						clouds.add(c);
					}
					synchronized (CloudManager.this) {
						CloudManager.this.notify();
					}
				} catch (QueryException e) {
					// Don't worry here, the method body will catch it
				}
			}
		});

		try {
			synchronized (this) {
				wait();
			}
			mWriteLock.lock();
			for (Cloud c : clouds) {
				mCachedClouds.put(c.getId(), c);
			}
			mWriteLock.unlock();
			return clouds;
		} catch (InterruptedException e) {
			throw new QueryException("Thread was interrupted", 418);
		}
	}
}
