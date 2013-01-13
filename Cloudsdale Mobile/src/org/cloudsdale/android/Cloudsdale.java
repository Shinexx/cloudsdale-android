package org.cloudsdale.android;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.cloudsdale.android.managers.CloudManager;
import org.cloudsdale.android.managers.FayeManager;
import org.cloudsdale.android.managers.NetworkManager;
import org.cloudsdale.android.managers.SessionManager;
import org.cloudsdale.android.managers.UserManager;
import org.cloudsdale.android.models.api.User;
import org.cloudsdale.android.models.parsers.GsonRoleAdapter;
import org.cloudsdale.android.network.CloudsdaleApiClient;

/**
 * Global application class
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class Cloudsdale extends Application {

	// Thirty minutes
	public final int			AVATAR_EXPIRATION	= 30 * 60 * 1000;
	// Sixty minutes
	public final int			CLOUD_EXPIRATION	= 1000 * 60 * 60;

	// API client
	private CloudsdaleApiClient	mApiClient;

	// objects
	private Gson				mJsonDeserializer;

	// Managers
	private SessionManager		mUserAccountManager;
	private UserManager			mUserManager;
	private FayeManager			mFayeManager;
	private NetworkManager		mNetManager;
	private CloudManager		mCloudManager;

	@Override
	public void onCreate() {
		mUserAccountManager = new SessionManager(this);
		mUserManager = new UserManager();
		mFayeManager = new FayeManager(this);
		mNetManager = new NetworkManager(this);

		super.onCreate();
	}

	/**
	 * Determines if the application is currently debuggable.
	 * 
	 * @return A boolean representing the debug status of the app.
	 */
	public boolean isDebuggable() {
		return (0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));
	}

	/**
	 * Convenience method to retrieve the current application context
	 * 
	 * @return The application context
	 */
	public Context getContext() {
		return this;
	}

	/**
	 * Convenience method to get a JSON deserializer configured to match
	 * Cloudsdale's quirks
	 * 
	 * @return A JSON deserializer (GSON)
	 */
	public Gson getJsonDeserializer() {
		if (mJsonDeserializer == null) {
			GsonBuilder builder = new GsonBuilder();
			builder.setDateFormat("yyyy/MM/dd HH:mm:ss Z");
			builder.registerTypeAdapter(User.Role.class, new GsonRoleAdapter());
			mJsonDeserializer = builder.create();
		}
		return mJsonDeserializer;
	}

	/**
	 * Gets the UserAccountManager instance for this application instance
	 * 
	 * @return The UserAccountManager attached to this application instance
	 */
	public SessionManager getUserAccountManager() {
		if (mUserAccountManager == null)
			mUserAccountManager = new SessionManager(this);
		return mUserAccountManager;
	}

	/**
	 * Gets the UserManager attached to this application instance
	 * 
	 * @return The UserManager attached to this application instance
	 */
	public UserManager getUserManager() {
		if (mUserManager == null) mUserManager = new UserManager();
		return mUserManager;
	}

	/**
	 * Gets the FayeManager attached to this application instance
	 * 
	 * @return The FayeManager attached to this application instance
	 */
	public FayeManager getFayeManager() {
		if (mFayeManager == null) mFayeManager = new FayeManager(this);
		return mFayeManager;
	}

	/**
	 * Gets the NetworkManager attached to this application instance
	 * 
	 * @return The NetworkManager attached to this application instance
	 */
	public NetworkManager getNetworkManager() {
		if (mNetManager == null) mNetManager = new NetworkManager(this);
		return mNetManager;
	}

	/**
	 * Gets the CloudManager attached to this application instance
	 * 
	 * @return The CloudManager attached to this application instance
	 */
	public CloudManager getNearestPegasus() {
		if (mCloudManager == null) mCloudManager = new CloudManager();
		return mCloudManager;
	}

	/**
	 * Gets the Cloudsale API client
	 * 
	 * @return The Cloudsdale API client in use by the application
	 */
	public CloudsdaleApiClient callZephyr() {
		if (mApiClient == null)
			mApiClient = new CloudsdaleApiClient(this);
		return mApiClient;
	}
}
