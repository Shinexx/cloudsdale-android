package org.cloudsdale.android;

import org.cloudsdale.android.ui.CloudListActivity;
import org.cloudsdale.android.ui.LoginActivity;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.bugsense.trace.BugSenseHandler;
import com.zubhium.ZubhiumSDK;

public class StartActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// SET THEMES HERE
		getSupportActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup_layout);
		
		// Setup BugSense
		BugSenseHandler.setup(this, "2bccbeb2");
		// Setup Zubhium
		ZubhiumSDK sdk = ZubhiumSDK.getZubhiumSDKInstance(
				getApplicationContext(), "65de0ea209fa414beee8518bd08b05");
		sdk.enableCrashReporting(true);
		
		if(PersistentData.getMe(this) != null) {
			Intent intent = new Intent();
			intent.setClass(this, CloudListActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else {
			Intent intent = new Intent();
			intent.setClass(this, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
	}

}
