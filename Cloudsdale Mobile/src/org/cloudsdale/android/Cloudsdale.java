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

	// Public data objects
	private static String				sCloudShowing;

	// Managers
	private static UserAccountManager	sUserAccountManager;
	private static UserManager			sUserManager;
	private static FayeManager			sFayeManager;
	private static NetworkManager		sNetManager;
	private static CloudManager			sCloudManager;

	@Override
	public void onCreate() {
		sAppObject = this;
		sUserAccountManager = new UserAccountManager();
		sUserManager = new UserManager();
		sFayeManager = new FayeManager(this);
		sNetManager = new NetworkManager();

		super.onCreate();
	}

	public static boolean isDebuggable() {
		return (0 != (sAppObject.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));
	}

	public static void setShowingCloud(String cloudId) {
		sCloudShowing = cloudId;
	}

	public static String getShowingCloud() {
		return sCloudShowing;
	}

	public static Context getContext() {
		return sAppObject;
	}

	public static Gson getJsonDeserializer() {
		if (sJsonDeserializer == null) {
			GsonBuilder builder = new GsonBuilder();
			builder.setDateFormat("yyyy/MM/dd HH:mm:ss Z");
			builder.registerTypeAdapter(Role.class, new GsonRoleAdapter());
			sJsonDeserializer = builder.create();
		}
		return sJsonDeserializer;
	}

	public static UserAccountManager getUserAccountManager() {
		if (sUserAccountManager == null)
			sUserAccountManager = new UserAccountManager();
		return sUserAccountManager;
	}

	public static UserManager getUserManager() {
		if (sUserManager == null) 
			sUserManager = new UserManager();
		return sUserManager;
	}
	
	public static FayeManager getFayeManager() {
		if(sFayeManager == null) 
			sFayeManager = new FayeManager(sAppObject);
		return sFayeManager;
	}
	
	public static NetworkManager getNetworkManager() {
		if(sNetManager == null)
			sNetManager = new NetworkManager();
		return sNetManager;
	}
	
	public static CloudManager getNearestPegasus() {
		if(sCloudManager == null)
			sCloudManager = new CloudManager();
		return sCloudManager;
	}
}
