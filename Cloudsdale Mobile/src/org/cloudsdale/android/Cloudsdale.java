package org.cloudsdale.android;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.IBinder;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.cloudsdale.android.faye.CloudsdaleFayeBinder;
import org.cloudsdale.android.faye.CloudsdaleFayeClient;
import org.cloudsdale.android.faye.CloudsdaleFayeListener;
import org.cloudsdale.android.faye.CloudsdaleFayeService;
import org.cloudsdale.android.faye.FayeMessageHandler;
import org.cloudsdale.android.managers.UserManager;
import org.cloudsdale.android.models.CloudsdaleFayeMessage;
import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.api.User;

import java.util.ArrayList;

/**
 * Global application class
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class Cloudsdale extends Application {

	// Debug fields
	public static final boolean						DEBUG				= true;
	private static final String						TAG					= "Cloudsdale Mobile";

	// Thirty minutes
	public static final int							AVATAR_EXPIRATION	= 30 * 60 * 1000;
	public static final int							CLOUD_EXPIRATION	= 1000 * 60 * 60;

	// Static objects
	private static Cloudsdale						sAppObject;
	private static Gson								sJsonDeserializer;

	// Public data objects
	private static String							sCloudShowing;

	/**
	 * Dummy constructor to handle creating static classes and fetch the global
	 * app context
	 */
	public Cloudsdale() {
		super();
		sAppObject = this;
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
			sJsonDeserializer = builder.create();
		}
		return sJsonDeserializer;
	}
}

