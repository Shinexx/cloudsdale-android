package org.cloudsdale.android;

import android.app.Application;
import android.content.Context;

public class Cloudsdale extends Application {
	
	private static Cloudsdale instance;
	
	public Cloudsdale() {
		super();
		instance = this;
		new PersistentData();
	}
	
	public static Context getContext() {
		return instance;
	}

}
