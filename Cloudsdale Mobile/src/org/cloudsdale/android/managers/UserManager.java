package org.cloudsdale.android.managers;

import android.accounts.Account;
import android.accounts.AccountManager;

import com.bugsense.trace.BugSenseHandler;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.api.User;
import org.cloudsdale.android.models.exceptions.QueryException;
import org.cloudsdale.android.models.network.CloudResponse;
import org.cloudsdale.android.models.network.UserResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Manager class designed to cache and retrieve Cloudsdale users. <br />
 * Copyright (c) 2012 Cloudsdale.org
 * 
 * @author Jamison Greeley (berwyn.codeweaver@gmail.com)
 * 
 */
public class UserManager extends ManagerBase {

	private HashMap<String, User>	mCachedUsers;

	public UserManager(Cloudsdale mAppInstance) {
		super(mAppInstance);
	}

	/**
	 * Gets the user that has logged into the application
	 * 
	 * @return The user that's logged in
	 * @throws QueryException
	 *             When the query cannot be completed
	 */
	public User getLoggedInUser() throws QueryException {
		Account activeAccount = mAppInstance.getSessionManager()
				.getActiveSession();
		AccountManager am = mAppInstance.getSessionManager()
				.getAccountManager();
		String id = am.getUserData(activeAccount, SessionManager.KEY_ID);
		User loggedInUser = getUserById(id);
		getCloudsForUser(loggedInUser);
		return loggedInUser;
	}

	/**
	 * Gets a user by ID, fetching from the API as necessary.
	 * 
	 * @param id
	 *            The ID of the user to load
	 * @return The user corresponding to the passed ID
	 * @throws QueryException
	 *             When the query cannot be completed
	 */
	public User getUserById(String id) throws QueryException {
		if (mCachedUsers.containsKey(id)) {
			mReadLock.lock();
			User returnUser = mCachedUsers.get(id);
			mReadLock.unlock();
			return returnUser;
		} else {
			final CountDownLatch latch = new CountDownLatch(1);
			mAppInstance.callZephyr().getUser(id,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(String json) {
							Gson gson = mAppInstance.getJsonDeserializer();
							UserResponse response = gson.fromJson(json,
									UserResponse.class);
							storeUser(response.getResult());
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
				User returnUser = mCachedUsers.get(id);
				mReadLock.unlock();
				return returnUser;
			} catch (InterruptedException e) {
				BugSenseHandler.sendException(e);
				throw new QueryException("Request was interrupted", 422);
			}
		}
	}

	/**
	 * Gets the clouds for a user from the API
	 * 
	 * @param user
	 *            The user to fetch clouds for
	 * @throws QueryException
	 *             When the query can't be completed
	 */
	private void getCloudsForUser(User user) throws QueryException {
		final CountDownLatch latch = new CountDownLatch(1);
		final ArrayList<Cloud> userClouds = new ArrayList<Cloud>();
		mAppInstance.callZephyr().getUserClouds(user.getId(),
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String json) {
						Gson gson = mAppInstance.getJsonDeserializer();
						ArrayList<Cloud> clouds = gson.fromJson(json,
								CloudResponse.class).getResults();
						userClouds.addAll(clouds);
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
			user.getClouds().addAll(userClouds);
			new Thread() {
				public void run() {
					for (Cloud c : userClouds) {
						mAppInstance.getNearestPegasus().storeCloud(c);
					}
				};
			}.run();
		} catch (InterruptedException e) {
			BugSenseHandler.sendException(e);
			throw new QueryException("Request interrupted", 422);
		}
	}

	/**
	 * Stores a user
	 * 
	 * @param user
	 *            The user to store
	 */
	public void storeUser(User user) {
		mWriteLock.lock();
		mCachedUsers.put(user.getId(), user);
		mWriteLock.unlock();
	}
}
