package org.cloudsdale.android.ui;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;
import org.cloudsdale.android.PersistentData;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.LoggedUser;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.api_models.Cloud;
import org.cloudsdale.android.models.queries.UserGetQuery;
import org.cloudsdale.android.ui.fragments.CloudDetailFragment;
import org.cloudsdale.android.ui.fragments.CloudListFragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class CloudListActivity extends SherlockFragmentActivity implements
		CloudListFragment.Callbacks {

	private boolean	mTwoPane;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_cloud_list);

		if (findViewById(R.id.cloud_detail_container) != null) {
			mTwoPane = true;
			((CloudListFragment) getSupportFragmentManager().findFragmentById(
					R.id.cloud_list)).setActivateOnItemClick(true);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		LoggedUser me = PersistentData.getMe(this);

		// data objects
		Resources res = getResources();
		QueryData data = new QueryData();
		ArrayList<BasicNameValuePair> headers = new ArrayList<BasicNameValuePair>();

		// Build the headers
		headers.add(new BasicNameValuePair("X-Auth-Token", me.getClientId()));

		// Put the data in the query object
		data.setUrl(res.getString(R.string.user_endpoint, me.getId()));
		data.setHeaders(headers);

		// Get the user from the API
		final UserGetQuery query = new UserGetQuery();
		final LoggedUser u = (LoggedUser) query.execute(data);
		u.setClientId(me.getClientId());

		// Write the user's cloud to the file system
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// Show the progress bar
				CloudListActivity.this.getSherlock()
						.setProgressBarIndeterminateVisibility(true);

				// Wait for the user query to finish
				while (query.isAlive()) {
					continue;
				}

				// Store all the user's clouds
				for (Cloud cloud : u.getClouds()) {
					PersistentData.storeCloud(CloudListActivity.this, cloud);
				}

				// Store the updated user
				PersistentData.StoreMe(CloudListActivity.this, u);

				// Hid the progress bar
				CloudListActivity.this.getSherlock()
						.setProgressBarIndeterminateVisibility(false);
			}
		});
		t.start();
	}

	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			Bundle arguments = new Bundle();
			arguments.putString(CloudDetailFragment.ARG_ITEM_ID, id);
			CloudDetailFragment fragment = new CloudDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.cloud_detail_container, fragment).commit();

		} else {
			Intent detailIntent = new Intent(this, CloudDetailActivity.class);
			detailIntent.putExtra(CloudDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}

	@Override
	public void onBackPressed() {
		// Go to the launcher
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);
	}
}
