package org.cloudsdale.android;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.support.v4.util.LruCache;

import org.cloudsdale.android.models.IdentityModel;
import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.api.Session;
import org.cloudsdale.android.models.api.User;

import java.util.concurrent.locks.ReentrantReadWriteLock;

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
public class DataStore<T extends IdentityModel> {

	private static final ReentrantReadWriteLock	LOCK				= new ReentrantReadWriteLock(
																			true);
	private static final int					CACHE_SIZE			= 20 * 1024 * 1024; // 20MiB
	protected static final String				KEY_USER_ACCOUNT_ID	= "id";

	protected static AccountManager				accountManager;
    protected static Account					activeAccount;
	protected static String						accountType;

	protected Cloudsdale						cloudsdale;

	protected LruCache<String, T>				cache;

	public DataStore(Cloudsdale cloudsdale) {
		cache = new LruCache<String, T>(CACHE_SIZE);
		this.cloudsdale = cloudsdale;
		if (accountManager == null) {
			accountManager = AccountManager.get(cloudsdale);
		}
		accountType = this.cloudsdale.getString(R.string.account_type);

	}

    public static Account getActiveAccount() {
        return activeAccount;
    }

    public static void setActiveAccount(Account activeAccount) {
        DataStore.activeAccount = activeAccount;
    }

	/**
	 * Persists an account to the system, based on the user in a given session
	 * 
	 * @param session
	 *            The session to generate an account (or update an account) from
	 * @return A boolean indicating whether the account was created
	 */
	public static boolean storeAccount(Session session) {
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
	public static Account[] getAccounts() {
		return accountManager.getAccountsByType(accountType);
	}

	/**
	 * Gets all the user IDs associated with persisted accounts
	 * 
	 * @return An array of String IDs for all persisted accounts
	 */
	public static String[] getAccountIds() {
		Account[] accounts = getAccounts();
		String[] ids = new String[accounts.length];
		for (int i = 0; i < accounts.length; i++) {
			ids[i] = accountManager.getUserData(accounts[i],
					KEY_USER_ACCOUNT_ID);
		}
		return ids;
	}

	public static String getActiveAccountID() {
		return accountManager.getUserData(activeAccount, KEY_USER_ACCOUNT_ID);
	}

	/**
	 * Not yet implemented
	 */
	public static void deleteAccount() {
		throw new UnsupportedOperationException();
		// TODO Delete accounts on demand
	}

	/**
	 * Stores a cloud in an LRU cache. See {@link android.util.LruCache} for
	 * cache details.
	 * 
	 * @param obj
	 *            The object to store
	 */
	public void store(T obj) {
		LOCK.writeLock().lock();
		cache.put(obj.getId(), obj);
		LOCK.writeLock().unlock();
	}

	/**
	 * Retrieves a cloud from the LRU cache, if it exists
	 * 
	 * @param id
	 *            The ID of the cloud to look up
	 * @return A {@link Cloud} if one is cached, null otherwise
	 */
	public T get(String id) {
		if(cache.size() == 0)
			return null;
		LOCK.readLock().lock();
		T returnVal = cache.get(id);
		LOCK.readLock().unlock();
		return returnVal;
	}

}
