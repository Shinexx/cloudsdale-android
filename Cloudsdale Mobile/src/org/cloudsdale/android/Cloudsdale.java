package org.cloudsdale.android;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.cloudsdale.android.managers.CloudManager;
import org.cloudsdale.android.managers.FayeManager;
import org.cloudsdale.android.managers.NetworkManager;
import org.cloudsdale.android.managers.UserAccountManager;
import org.cloudsdale.android.managers.UserManager;
import org.cloudsdale.android.models.Role;
import org.cloudsdale.android.models.parsers.GsonRoleAdapter;
import org.cloudsdale.android.ui.fragments.SlidingMenuFragment;

/**
 * Global application class
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class Cloudsdale extends Application {

	// Thirty minutes
	public static final int				AVATAR_EXPIRATION	= 30 * 60 * 1000;
	public static final int				CLOUD_EXPIRATION	= 1000 * 60 * 60;

	// Static objects
	private static Cloudsdale			sAppObject;
	private static Gson					sJsonDeserializer;

	// Managers
	private static UserAccountManager	sUserAccountManager;
	private static UserManager			sUserManager;
	private static FayeManager			sFayeManager;
	private static NetworkManager		sNetManager;
	private static CloudManager			sCloudManager;

	// Our current
	private static SlidingMenuFragment	sSlidingMenu;

	@Override
	public void onCreate() {
		sAppObject = this;
		sUserAccountManager = new UserAccountManager();
		sUserManager = new UserManager();
		sFayeManager = new FayeManager(this);
		sNetManager = new NetworkManager();

		super.onCreate();
	}

	/**
	 * Determines if the application is currently debuggable.
	 * 
	 * @return A boolean representing the debug status of the app.
	 */
	public static boolean isDebuggable() {
		return (0 != (sAppObject.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));
	}

	/**
	 * Convenience method to retrieve the current application context
	 * 
	 * @return The application context
	 */
	public static Context getContext() {
		return sAppObject;
	}

	/**
	 * Convenience method to get a JSON deserializer configured to match
	 * Cloudsdale's quirks
	 * 
	 * @return A JSON deserializer (GSON)
	 */
	public static Gson getJsonDeserializer() {
		if (sJsonDeserializer == null) {
			GsonBuilder builder = new GsonBuilder();
			builder.setDateFormat("yyyy/MM/dd HH:mm:ss Z");
			builder.registerTypeAdapter(Role.class, new GsonRoleAdapter());
			sJsonDeserializer = builder.create();
		}
		return sJsonDeserializer;
	}

	/**
	 * Gets the UserAccountManager instance for this application instance
	 * 
	 * @return The UserAccountManager attached to this application instance
	 */
	public static UserAccountManager getUserAccountManager() {
		if (sUserAccountManager == null)
			sUserAccountManager = new UserAccountManager();
		return sUserAccountManager;
	}

	/**
	 * Gets the UserManager attached to this application instance
	 * 
	 * @return The UserManager attached to this application instance
	 */
	public static UserManager getUserManager() {
		if (sUserManager == null) sUserManager = new UserManager();
		return sUserManager;
	}

	/**
	 * Gets the FayeManager attached to this application instance
	 * 
	 * @return The FayeManager attached to this application instance
	 */
	public static FayeManager getFayeManager() {
		if (sFayeManager == null) sFayeManager = new FayeManager(sAppObject);
		return sFayeManager;
	}

	/**
	 * Gets the NetworkManager attached to this application instance
	 * 
	 * @return The NetworkManager attached to this application instance
	 */
	public static NetworkManager getNetworkManager() {
		if (sNetManager == null) sNetManager = new NetworkManager();
		return sNetManager;
	}

	/**
	 * Gets the CloudManager attached to this application instance
	 * 
	 * @return The CloudManager attached to this application instance
	 */
	public static CloudManager getNearestPegasus() {
		if (sCloudManager == null) sCloudManager = new CloudManager();
		return sCloudManager;
	}

	/**
	 * Gets the currently active SlidingMenu fragment we're using to navigate
	 * 
	 * @return The active SlidingMenu fragment
	 */
	public static SlidingMenuFragment getSlidingMenu() {
		if (sSlidingMenu == null) sSlidingMenu = new SlidingMenuFragment();
		return sSlidingMenu;
	}
}
