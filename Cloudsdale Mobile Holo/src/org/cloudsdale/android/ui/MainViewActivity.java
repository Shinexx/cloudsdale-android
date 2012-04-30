package org.cloudsdale.android.ui;

import org.cloudsdale.android.models.Cloud;
import org.cloudsdale.android.models.User;
import org.cloudsdale.logic.CloudsdaleAsyncQueryWrapper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;

public class MainViewActivity extends Activity {

	public static final String TAG = "CloudsdaleMainViewActivity";
	
	private User me;
	private ListView mCloudList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "Activity State: onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
		
		// Bind ListView
		mCloudList = (ListView) findViewById(R.id.main_view_root);
		
		// Bind the user
		SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
		String meString = sharedPrefs.getString("me", "");
		me = new Gson().fromJson(meString, User.class);
		
		if (me == null) { 
			Intent intent = new Intent();
			intent.setClass(this, LoginActivity.class);
			startActivity(intent);
		}
	}
	
	private void populateCloudList() {
		// Build the query object
		CloudsdaleAsyncQueryWrapper wrapper = new CloudsdaleAsyncQueryWrapper();
		wrapper.query(new String[] { getString(R.string.cloudsdale_dev_api_url) + "/" + me.getId() + "/clouds" }, Cloud.class, );
	}
}
