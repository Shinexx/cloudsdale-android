package org.cloudsdale.android;

import android.app.Application;

/**
 * Global application class
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 *
 */
public class Cloudsdale extends Application {

	/**
	 * Dummy constructor to handle creating static classes and fetch the global
	 * app context
	 */
	public Cloudsdale() {
		super();
		new PersistentData();
	}

}
