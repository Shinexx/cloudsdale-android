package org.cloudsdale.android;

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
import com.googlecode.androidannotations.annotations.EApplication;
import com.googlecode.androidannotations.annotations.SystemService;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;

import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.api.User;
import org.cloudsdale.android.models.parsers.GsonRoleAdapter;
import org.cloudsdale.android.network.CloudsdaleApiClient;
import org.codeweaver.remoteconfiguredhttpclient.RemoteConfigurationListener;
import org.json.JSONException;
import org.json.JSONObject;


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

	// API clients
	private CloudsdaleApiClient			cloudsdaleApi;

	// objects
	private Gson						mJsonDeserializer;
	@StringRes(R.string.config_url)
	String								configUrl;
	@StringRes(R.string.facebook_app_key_debug)
	String								facebookDebugKey;
	@StringRes(R.string.facebook_app_key)
	String								facebookKey;
	private JsonObject					mConfig;
	private RemoteConfigurationListener	configListener;
	private ConfigurationTask			configTask;

	// Managers
	@SystemService
	ConnectivityManager					connectivityManager;
	private DataStore<Cloud>			cloudDataStore;
	private DataStore<User>				userDataStore;

	@Override
	public void onCreate() {
		cloudDataStore = new DataStore<Cloud>(this);
		userDataStore = new DataStore<User>(this);
		cloudsdaleApi = new CloudsdaleApiClient(this);
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

	public void configure(final RemoteConfigurationListener configListener) {

		this.configListener = configListener;

		cloudsdaleApi.getRemoteConfiguration(configUrl,
				new AsyncHttpClient.JSONObjectCallback() {

					@Override
					public void onCompleted(Exception e,
							AsyncHttpResponse source, final JSONObject result) {
						if (e != null) {
							// TODO Handle exception
							if (isDebuggable()) {
								Log.e(TAG, "Error during configure request");
								e.printStackTrace();
							}
							configListener.onConfigurationFailed(e, result);
							return;
						} else {
							configTask = new ConfigurationTask();
							configTask.execute(result.toString());
						}
					}
				});
	}

	public void stopConfiguration() {
		if (configTask != null) {
			configTask.cancel(true);
			configTask = null;
		}
	}

	/**
	 * Configures our remote services, given a list of services
	 * 
	 * @param services
	 *            The JsonArray list of service objects
	 * @throws JSONException
	 */
	private void configureApiServices(JsonArray services) throws JSONException {
		for (JsonElement element : services) {
			JSONObject obj = new JSONObject(element.toString());
			String id = obj.optString("id");
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

	class ConfigurationTask extends AsyncTask<String, Void, Void> {

		private JSONObject	rawResult;

		@Override
		protected Void doInBackground(String... params) {
			try {
				rawResult = new JSONObject(params[0]);
				mConfig = new JsonParser().parse(params[0]).getAsJsonObject();
				JsonArray services = mConfig.getAsJsonArray(SERVICES_JSON_KEY);
				configureApiServices(services);
			} catch (JSONException e) {
				if (isDebuggable()) {
					Log.e(TAG, "Error during configure request");
					e.printStackTrace();
				}
				cancel(true);
			}
			return null;
		}

		@Override
		protected void onCancelled() {
			if (configListener != null) {
				configListener.onConfigurationFailed(null, null);
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			if (configListener != null) {
				configListener.onConfigurationSucceeded(200, rawResult);
			}
			super.onPostExecute(result);
		}
	}

}
