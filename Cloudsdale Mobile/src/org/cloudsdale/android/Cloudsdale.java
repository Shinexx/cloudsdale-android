package org.cloudsdale.android;

import org.cloudsdale.android.models.Role;
import org.cloudsdale.android.models.parsers.GsonRoleAdapter;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Global application class
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class Cloudsdale extends Application {

	// Debug fields
	public static final boolean						DEBUG				= true;

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
			builder.setDateFormat("yyyy/MM/dd HH:mm:ss Z");
			builder.registerTypeAdapter(Role.class, new GsonRoleAdapter());
			sJsonDeserializer = builder.create();
		}
		return sJsonDeserializer;
	}
}

