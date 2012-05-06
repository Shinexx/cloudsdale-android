package org.cloudsdale.android.ui;

import org.cloudsdale.android.logic.PersistentData;
import org.cloudsdale.android.models.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

public class MainViewActivity extends Activity {

	public static final String	TAG	= "CloudsdaleMainViewActivity";

	private User				me;
	private LinearLayout		mCloudList;

	/**
	 * @see andoid.app.Activity#onCreate()
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "Activity State: onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);

		// Bind ListView
		mCloudList = (LinearLayout) findViewById(R.id.main_view_root);

		// Bind the user
		me = PersistentData.getMe();

		// TODO If the current user isn't in PersistentData, check the DB or force
		// the user to login
		if (me == null) {
			// TODO pull from the DB
			
			Intent intent = new Intent();
			intent.setClass(this, LoginActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		// TODO Display clouds and update the list
		populateCloudList();
	}

	/**
	 * First fetch the existing list of clouds, then populate the new ones
	 */
	private void populateCloudList() {
		// TODO Fetch the current clouds
		// TODO Start by checking if the PersistentData has any clouds
		// TODO Then if it doesn't, yoink them out of the DB

		// TODO Build the query object
		// TODO Shunt all that crap into Persistent Data
	}
}
