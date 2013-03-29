package org.cloudsdale.android;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EApplication;
import com.googlecode.androidannotations.annotations.SystemService;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;

import org.cloudsdale.android.managers.FayeManager;
import org.cloudsdale.android.managers.NetworkManager;
import org.cloudsdale.android.models.api.User;
import org.cloudsdale.android.models.parsers.GsonRoleAdapter;
import org.cloudsdale.android.network.CloudsdaleApiClient;
import org.json.JSONException;
import org.json.JSONObject;

import lombok.Getter;
import lombok.val;

/**
 * Global application class
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
@EApplication
public class Cloudsdale extends Application {

	// Thirty minutes
	public static final int		AVATAR_EXPIRATION	= 30 * 60 * 1000;
	// Sixty minutes
	public static final int		CLOUD_EXPIRATION	= 1000 * 60 * 60;
	private static final String	DATE_FORMAT			= "yyyy/MM/dd HH:mm:ss Z";
	private static final String	SERVICES_JSON_KEY	= "services";

	// API clients
	@Bean
	CloudsdaleApiClient			cloudsdaleApi;

	// objects
	private Gson				mJsonDeserializer;
	@StringRes(R.string.config_url)
	String						configUrl;
	@Getter
	private JsonObject			mConfig;

	// Managers
	@SystemService
	ConnectivityManager			connectivityManager;
	@Bean
	@Getter
	DataStore					dataStore;
	private FayeManager			mFayeManager;

	@Override
	public void onCreate() {
		mFayeManager = new FayeManager(this);

		cloudsdaleApi.getRemoteConfiguration(configUrl,
				new AsyncHttpClient.JSONObjectCallback() {

					@Override
					public void onCompleted(Exception e,
							AsyncHttpResponse source, JSONObject result) {
						if (e != null) {
							// TODO Handle exception
							e.printStackTrace();
						} else {
							mConfig = getJsonDeserializer().fromJson(
									result.toString(), JsonObject.class);
							try {
								configureApiServices(mConfig
										.getAsJsonArray(SERVICES_JSON_KEY));
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				});

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

	public boolean hasInternetConnection() {
		val activeNetInfo = connectivityManager.getActiveNetworkInfo();
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
	 * Gets the FayeManager attached to this application instance
	 * 
	 * @return The FayeManager attached to this application instance
	 */
	public FayeManager getFayeManager() {
		if (mFayeManager == null) mFayeManager = new FayeManager(this);
		return mFayeManager;
	}

	/**
	 * Gets the Cloudsale API client
	 * 
	 * @return The Cloudsdale API client in use by the application
	 */
	public CloudsdaleApiClient callZephyr() {
		return cloudsdaleApi;
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
	 * Configures our remote services, given a list of services
	 * 
	 * @param services
	 *            The JsonArray list of service objects
	 * @throws JSONException
	 */
	public void configureApiServices(JsonArray services) throws JSONException {
		for (JsonElement element : services) {
			val obj = new JSONObject(element.getAsString());
			val id = obj.optString("id");
			if (id.equals("cloudsdale")) {
				cloudsdaleApi.configure(obj);
			} else if (id.equals("cloudsdale-faye")) {
				// TODO Configure Faye resources
			} else if (id.equals("my-little-face-when")) {
				// TODO Configure MLFW
			} else if (id.equals("derpibooru")) {
				// TODO Configure Derpibooru
			}
		}
	}

}
