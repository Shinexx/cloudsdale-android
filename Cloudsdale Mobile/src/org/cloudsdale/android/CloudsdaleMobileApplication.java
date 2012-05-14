package org.cloudsdale.android;

import org.cloudsdale.android.logic.PersistentData;

import android.app.Application;

public class CloudsdaleMobileApplication extends Application {
	
	public CloudsdaleMobileApplication() {
		super();
		
		PersistentData pd = new PersistentData();
	}

}
