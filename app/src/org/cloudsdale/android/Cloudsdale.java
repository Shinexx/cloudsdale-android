package org.cloudsdale.android;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.cloudsdale.android.managers.CloudManager;
import org.cloudsdale.android.managers.FayeManager;
import org.cloudsdale.android.managers.NetworkManager;
import org.cloudsdale.android.managers.SessionManager;
import org.cloudsdale.android.managers.UserManager;
import org.cloudsdale.android.models.api.User;
import org.cloudsdale.android.models.parsers.GsonRoleAdapter;
import org.cloudsdale.android.network.CloudsdaleApiClient;

import lombok.Getter;
import lombok.Setter;

/**
 * Global application class
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class Cloudsdale extends Application {

	// Thirty minutes
	public static final int		AVATAR_EXPIRATION	= 30 * 60 * 1000;
	// Sixty minutes
	public static final int		CLOUD_EXPIRATION	= 1000 * 60 * 60;

	// API client
	private CloudsdaleApiClient	mApiClient;

	// objects
	private Gson				mJsonDeserializer;
	@Getter
	@Setter
	private JsonObject			mConfig;

	// Managers
	private UserManager			mUserManager;
	private FayeManager			mFayeManager;
	private NetworkManager		mNetManager;
	private CloudManager		mCloudManager;
	private SessionManager		mSessionManager;

	@Override
	public void onCreate() {
		mUserManager = new UserManager(this);
		mFayeManager = new FayeManager(this);
		mNetManager = new NetworkManager(this);
		mSessionManager = new SessionManager(this);

		super.onCreate();
	}

	/**
	 * Determines if the application is currently debuggable.
	 * 
	 * @return A boolean representing the debug status of the app.
	 */
	public boolean isDebuggable() {
		return BuildConfig.DEBUG;
	}

	/**
	 * Determines if our API client has been configured and is ready to request
	 * resources
	 * 
	 * @return a boolean stating whether the client is configured
	 */
	public boolean isConfigured() {
		return false;
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
	 * Gets the UserManager attached to this application instance
	 * 
	 * @return The UserManager attached to this application instance
	 */
	public UserManager getUserManager() {
		if (mUserManager == null) mUserManager = new UserManager(this);
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
		if (mCloudManager == null) mCloudManager = new CloudManager(this);
		return mCloudManager;
	}

	/**
	 * Gets the Cloudsale API client
	 * 
	 * @return The Cloudsdale API client in use by the application
	 */
	public CloudsdaleApiClient callZephyr() {
		if (mApiClient == null) mApiClient = new CloudsdaleApiClient(this);
		return mApiClient;
	}

	public SessionManager getSessionManager() {
		if (mSessionManager == null)
			mSessionManager = new SessionManager(this);
		return mSessionManager;
	}

	/**
	 * Gets the appropriate Facebook application key depending on debuggable
	 * mode (determined at runtime)
	 * 
	 * @return The Facebook application key
	 */
	public String getFacebookAppKey() {
		if (isDebuggable()) {
			return getString(R.string.facebook_app_key_debug);
		} else {
			return getString(R.string.facebook_app_key);
		}
	}

	/**
	 * Fetches the remote config JSON and configures our API clients
	 */
	public void configureFromRemote(JsonObject config) {
		mConfig = config;
		// TODO configure the services
	}

	public void configureApiServices(JsonArray services) {
		// TODO configure the APIs
	}

}
