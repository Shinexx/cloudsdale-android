package org.cloudsdale.android;

import android.app.Application;
import android.content.Context;

/**
 * Global application class
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 *
 */
public class Cloudsdale extends Application {

	private static Cloudsdale	instance;

	/**
	 * Dummy constructor to handle creating static classes and fetch the global
	 * app context
	 */
	public Cloudsdale() {
		super();
		instance = this;
		new PersistentData();
	}

	/**
	 * A helper method to get the global application context from anywhere
	 * 
	 * @return The global application context
	 */
	public static Context getContext() {
		return instance;
	}

}
