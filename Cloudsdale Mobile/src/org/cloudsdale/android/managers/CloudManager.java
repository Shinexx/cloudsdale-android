package org.cloudsdale.android.managers;

import com.bugsense.trace.BugSenseHandler;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.exceptions.QueryException;
import org.cloudsdale.android.models.network.CloudResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Manager class for Cloud fetching and caching. <br />
 * Copyright (c) 2012 Cloudsdale.org
 * 
 * @author Jamsion Greeley (berwyn.codeweaver@gmail.com)
 * 
 */
public class CloudManager extends ManagerBase {

	private HashMap<String, Cloud>	mCachedClouds;

	public CloudManager(Cloudsdale mAppInstance) {
		super(mAppInstance);
		mCachedClouds = new HashMap<String, Cloud>();
	}

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

	/**
	 * Gets a cloud, either from cache or from the API as necessary
	 * 
	 * @param id
	 *            The cloud's ID
	 * @return The cloud corresponding to the given ID
	 * @throws QueryException
	 */
	public Cloud getCloud(String id) throws QueryException {
		if (mCachedClouds.containsKey(id)) {
			mReadLock.lock();
			Cloud returnCloud = mCachedClouds.get(id);
			mReadLock.unlock();
			return returnCloud;
		} else {
			final CountDownLatch latch = new CountDownLatch(1);
			mAppInstance.callZephyr().getCloud(id,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(String json) {
							Gson gson = mAppInstance.getJsonDeserializer();
							CloudResponse response = gson.fromJson(json,
									CloudResponse.class);
							storeCloud(response.getResult());
							super.onSuccess(json);
						}

						@Override
						public void onFinish() {
							latch.countDown();
							super.onFinish();
						}

					});
			try {
				latch.await();
				mReadLock.lock();
				Cloud returnCloud = mCachedClouds.get(id);
				mReadLock.unlock();
				return returnCloud;
			} catch (InterruptedException e) {
				BugSenseHandler.sendException(e);
				throw new QueryException("Request interrupted", 422);
			}
		}
	}

	public ArrayList<Cloud> getCloudsForUser(String userId)
			throws QueryException {
		final CountDownLatch latch = new CountDownLatch(1);
		final ArrayList<Cloud> returnClouds = new ArrayList<Cloud>();
		mAppInstance.callZephyr().getUserClouds(userId,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String json) {
						Gson gson = mAppInstance.getJsonDeserializer();
						CloudResponse response = gson.fromJson(json,
								CloudResponse.class);
						returnClouds.addAll(response.getResults());
						super.onSuccess(json);
					}

					@Override
					public void onFinish() {
						latch.countDown();
						super.onFinish();
					}

				});
		try {
			latch.await();
			new Thread(){
				
				public void run() {
					for(Cloud c : returnClouds) {
						storeCloud(c);
					}
				};
				
			}.run();
			return returnClouds;
		} catch (InterruptedException e) {
			BugSenseHandler.sendException(e);
			throw new QueryException("Request interrupted", 422);
		}
	}
}
