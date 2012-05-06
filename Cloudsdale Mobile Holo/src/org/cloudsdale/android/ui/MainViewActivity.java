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

		if (me == null) {
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
		// TODO Display clouds and update the list
		super.onResume();
		populateCloudList();
	}

	/**
	 * First fetch the existing list of clouds, then populate the new ones
	 */
	private void populateCloudList() {
		// Build the query object
	}
}
