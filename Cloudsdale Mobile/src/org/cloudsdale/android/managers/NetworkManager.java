package org.cloudsdale.android.managers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.cloudsdale.android.Cloudsdale;

/**
 * Manager class to report network state and manage network queries Copyright
 * (c) 2012 mAppInstance.org
 * 
 * @author Jamison Greeley atomicrat2552@gmail.com
 * 
 */
public class NetworkManager {

	// Connectivity status objects
	private ConnectivityManager	mConnectivityManager;
	private Cloudsdale		mAppInstance;

	public NetworkManager(Cloudsdale mAppInstance) {
		mAppInstance = mAppInstance;
	}

	public boolean isInternetConnected() {
		if (mConnectivityManager == null) {
			mConnectivityManager = (ConnectivityManager) mAppInstance
					.getContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
		}

		return mConnectivityManager.getActiveNetworkInfo().isConnected();
	}

	public boolean isMobileNetConnected() {
		if (mConnectivityManager == null) {
			mConnectivityManager = (ConnectivityManager) mAppInstance
					.getContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
		if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
			return info.isConnected();
		} else
			return false;
	}

	public boolean isWimaxConnected() {
		if (mConnectivityManager == null) {
			mConnectivityManager = (ConnectivityManager) mAppInstance
					.getContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
		if (info.getType() == ConnectivityManager.TYPE_WIMAX) {
			return info.isConnected();
		} else
			return false;
	}

	public boolean isWifiConnected() {
		if (mConnectivityManager == null) {
			mConnectivityManager = (ConnectivityManager) mAppInstance
					.getContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
		if (info.getType() == ConnectivityManager.TYPE_WIFI) {
			return info.isConnected();
		} else
			return false;
	}

}
