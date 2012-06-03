package org.cloudsdale.android;

import org.cloudsdale.android.logic.PersistentData;
import com.bugsense.trace.BugSenseHandler;

import android.app.Application;
import android.content.Context;

public class CloudsdaleMobileApplication extends Application {
	
	private static CloudsdaleMobileApplication instance;
	
	public CloudsdaleMobileApplication() {
		super();
		instance = this;
		new PersistentData();
	}
	
	public static Context getContext() {
		return instance;
	}

}
