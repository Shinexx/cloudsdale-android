package org.cloudsdale.android.managers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.Provider;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.LoggedUser;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.exceptions.QueryException;
import org.cloudsdale.android.models.queries.SessionQuery;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Manager class to report network state and manage network queries
 * Copyright (c) 2012 Cloudsdale.org
 * 
 * @author Jamison Greeley atomicrat2552@gmail.com
 * 
 */
public class NetworkManager {

	// Connectivity status objects
	private static ConnectivityManager	sConnectivityManager;

	private static LoggedUser			sLoggedUser;
	private static QueryException		sExceptionThrown;

	public static boolean isInternetConnected() {
		if (sConnectivityManager == null) {
			sConnectivityManager = (ConnectivityManager) Cloudsdale
					.getContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
		}

		return sConnectivityManager.getActiveNetworkInfo().isConnected();
	}

	public static boolean isMobileNetConnected() {
		if (sConnectivityManager == null) {
			sConnectivityManager = (ConnectivityManager) Cloudsdale
					.getContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		NetworkInfo info = sConnectivityManager.getActiveNetworkInfo();
		if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
			return info.isConnected();
		} else return false;
	}

	public static boolean isWimaxConnected() {
		if (sConnectivityManager == null) {
			sConnectivityManager = (ConnectivityManager) Cloudsdale
					.getContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		NetworkInfo info = sConnectivityManager.getActiveNetworkInfo();
		if (info.getType() == ConnectivityManager.TYPE_WIMAX) {
			return info.isConnected();
		} else return false;
	}

	public static boolean isWifiConnected() {
		if (sConnectivityManager == null) {
			sConnectivityManager = (ConnectivityManager) Cloudsdale
					.getContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		NetworkInfo info = sConnectivityManager.getActiveNetworkInfo();
		if (info.getType() == ConnectivityManager.TYPE_WIFI) {
			return info.isConnected();
		} else return false;
	}

	/*
	 * Public method to authenticate users via Cloudsdale credentials
	 */
	public static LoggedUser authenticate(String email, String password)
			throws QueryException {
		// Reset the exception
		sExceptionThrown = null;

		// Get the api endpoint
		final String apiBase = Cloudsdale.getContext().getString(
				R.string.cloudsdale_api_base);
		final String sessionEndpoint = Cloudsdale.getContext().getString(
				R.string.cloudsdale_sessions_endpoint);
		final String apiUrl = apiBase + sessionEndpoint;

		// Build the query data
		try {
			final QueryData data = new QueryData();
			data.setUrl(apiUrl);
			JSONObject json = new JSONObject();
			json.put("email", email);
			json.put("password", password);
			data.setJson(json.toString());

			// Execute the query
			new Thread() {
				public void run() {
					SessionQuery query = new SessionQuery(apiUrl);
					try {
						sLoggedUser = query.execute(data,
								Cloudsdale.getContext());
					} catch (QueryException e) {
						sExceptionThrown = e;
					}
				};
			}.start();
		} catch (JSONException e1) {
			// TODO Handle this shit gracefully
			e1.printStackTrace();
		}
		if (sExceptionThrown != null) {
			return sLoggedUser;
		} else {
			throw sExceptionThrown;
		}
	}

	/*
	 * Public method to authenticate users via oAuth credentials (e.g. Twitter,
	 * Facebook)
	 */
	public static LoggedUser authenticate(String oAuthId, Provider oAuthProvider) {
		// TODO authenticate using oAuth credentials
		return null;
	}

}
