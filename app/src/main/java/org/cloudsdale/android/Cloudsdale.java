package org.cloudsdale.android;

import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.res.StringRes;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.api.User;
import org.cloudsdale.android.models.parsers.GsonRoleAdapter;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.IonBodyParamsRequestBuilder;
import com.koushikdutta.ion.builder.IonLoadRequestBuilder;


/**
 * Global application class
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
@EApplication
public class Cloudsdale extends Application {

	private static final String			TAG					= "Cloudsdale";

	// Thirty minutes
	public static final int				AVATAR_EXPIRATION	= 30 * 60 * 1000;
	// Sixty minutes
	public static final int				CLOUD_EXPIRATION	= 1000 * 60 * 60;
	private static final String			DATE_FORMAT			= "yyyy/MM/dd HH:mm:ss Z";
	private static final String			SERVICES_JSON_KEY	= "services";

	// objects
	private Gson						mJsonDeserializer;
	@StringRes(R.string.config_url)
	String								configUrl;
	@StringRes(R.string.facebook_app_key_debug)
	String								facebookDebugKey;
	@StringRes(R.string.facebook_app_key)
	String								facebookKey;
	private JsonObject					mConfig;

	// Managers
	@SystemService
	ConnectivityManager					connectivityManager;
	private DataStore<Cloud>			cloudDataStore;
	private DataStore<User>				userDataStore;

	@Override
	public void onCreate() {
		cloudDataStore = new DataStore<Cloud>(this);
		userDataStore = new DataStore<User>(this);
		super.onCreate();
	}

    public DataStore<Cloud> getCloudDataStore() {
        return cloudDataStore;
    }

    public DataStore<User> getUserDataStore() {
        return userDataStore;
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

	public boolean hasInternetConnection() {
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo == null) {
			return false;
		} else {
			return activeNetInfo.isConnected();
		}
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
			builder.setDateFormat(DATE_FORMAT);
			builder.registerTypeAdapter(User.Role.class, new GsonRoleAdapter());
			mJsonDeserializer = builder.create();
		}
		return mJsonDeserializer;
	}

	/**
	 * Gets the appropriate Facebook application key depending on debuggable
	 * mode (determined at runtime)
	 * 
	 * @return The Facebook application key
	 */
	public String getFacebookAppKey() {
		if (isDebuggable()) {
			return facebookDebugKey;
		} else {
			return facebookKey;
		}
	}

	/**
	 * Convenience method to get the cached user for the logged in user
	 * 
	 * @return The cached logged in user
	 */
	public User getLoggedInUser() {
		String id = DataStore.getActiveAccountID();
		return userDataStore.get(id);
	}

    public static class API {

        static <T> IonBodyParamsRequestBuilder Request(Context context, String resource, FutureCallback<T> callback) {
            return null;
        }

    }

}
