package org.cloudsdale.android;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.support.v4.util.LruCache;

import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.res.StringRes;

import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.api.Session;
import org.cloudsdale.android.models.api.User;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import lombok.Getter;
import lombok.Setter;
import lombok.val;

/**
 * Data storage class, utilizing LRU caches of users and clouds, as well as
 * managing account persistence via the Android account mechanism<br>
 * <br>
 * Copyright (c) 2013 Cloudsdale.org
 * 
 * @author Jamison Greeley <berwyn.codeweaver@gmail.com>
 * @since 0.2.0
 * @version 1.0
 * 
 */
@EBean
public class DataStore {

	private static final ReentrantReadWriteLock	CLOUD_LOCK			= new ReentrantReadWriteLock(
																			true);
	private static final ReentrantReadWriteLock	USER_LOCK			= new ReentrantReadWriteLock(
																			true);
	private static final int					CACHE_SIZE			= 20 * 1024 * 1024; // 20MiB
	private static final String					KEY_USER_ACCOUNT_ID	= "id";

	@App
	Cloudsdale									cloudsdale;
	@StringRes(R.string.account_type)
	String										accountType;

	private LruCache<String, Cloud>				clouds;
	private LruCache<String, User>				users;
	private AccountManager						accountManager;
	@Getter
	@Setter
	private Account								activeAccount;

	public DataStore() {
		clouds = new LruCache<String, Cloud>(CACHE_SIZE);
		users = new LruCache<String, User>(CACHE_SIZE);
		accountManager = AccountManager.get(cloudsdale);
	}

	/**
	 * Stores a cloud in an LRU cache. See {@link android.util.LruCache} for
	 * cache details.
	 * 
	 * @param cloud
	 *            The cloud to store
	 */
	public void storeCloud(Cloud cloud) {
		CLOUD_LOCK.writeLock().lock();
		clouds.put(cloud.getId(), cloud);
		CLOUD_LOCK.writeLock().unlock();
	}

	/**
	 * Retrieves a cloud from the LRU cache, if it exists
	 * 
	 * @param id
	 *            The ID of the cloud to look up
	 * @return A {@link Cloud} if one is cached, null otherwise
	 */
	public Cloud getCloud(String id) {
		CLOUD_LOCK.readLock().lock();
		val returnVal = clouds.get(id);
		CLOUD_LOCK.readLock().unlock();
		return returnVal;
	}

	/**
	 * Stores a user in an LRU cache. See {@link android.util.LruCache} for
	 * cache details.
	 * 
	 * @param user
	 *            The user to store
	 */
	public void storeUser(User user) {
		USER_LOCK.writeLock().lock();
		users.put(user.getId(), user);
		USER_LOCK.writeLock().unlock();
	}

	/**
	 * Retrieves a user from the LRU cache, if it exists
	 * 
	 * @param id
	 *            The ID of the user to look up
	 * @return A {@link User} if one is cached, null otherwise
	 */
	public User getUser(String id) {
		USER_LOCK.readLock().lock();
		val returnVal = users.get(id);
		USER_LOCK.readLock().unlock();
		return returnVal;
	}

	/**
	 * Persists an account to the system, based on the user in a given session
	 * 
	 * @param session
	 *            The session to generate an account (or update an account) from
	 * @return A boolean indicating whether the account was created
	 */
	public boolean storeAccount(Session session) {
		User user = session.getUser();
		Account account = new Account(user.getName(), accountType);
		boolean accountCreated = accountManager.addAccountExplicitly(account,
				user.getAuthToken(), null);
		if (accountCreated) {
			accountManager.setUserData(account, KEY_USER_ACCOUNT_ID,
					user.getId());
			activeAccount = account;
			return accountCreated;
		} else {
			accountManager.setPassword(account, user.getAuthToken());
			activeAccount = account;
			return true;
		}
	}

	/**
	 * Retrieves all accounts currently persisted to the system
	 * 
	 * @return An array of {@link Account} objects for all persisted accounts
	 */
	public Account[] getAccounts() {
		return accountManager.getAccountsByType(accountType);
	}

	/**
	 * Gets all the user IDs associated with persisted accounts
	 * 
	 * @return An array of String IDs for all persisted accounts
	 */
	public String[] getAccountIds() {
		val accounts = getAccounts();
		String[] ids = new String[accounts.length];
		for (int i = 0; i < accounts.length; i++) {
			ids[i] = accountManager.getUserData(accounts[i],
					KEY_USER_ACCOUNT_ID);
		}
		return ids;
	}

	/**
	 * Not yet implemented
	 */
	public void deleteAccount() {
		throw new UnsupportedOperationException();
		// TODO Delete accounts on demand
	}

}
