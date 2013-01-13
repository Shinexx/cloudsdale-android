package org.cloudsdale.android.managers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.cloudsdale.android.Cloudsdale;

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

	public boolean isInternetConnected() {
		if (sConnectivityManager == null) {
			sConnectivityManager = (ConnectivityManager) Cloudsdale
					.getContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
		}

		return sConnectivityManager.getActiveNetworkInfo().isConnected();
	}

	public boolean isMobileNetConnected() {
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

	public boolean isWimaxConnected() {
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

	public boolean isWifiConnected() {
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

}
