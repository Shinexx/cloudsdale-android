package org.cloudsdale.android.ui;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

import org.cloudsdale.android.R;
import org.cloudsdale.android.ui.fragments.CloudDetailFragment;
import org.cloudsdale.android.ui.fragments.CloudListFragment;

public class CloudListActivity extends SherlockFragmentActivity implements
		CloudListFragment.Callbacks {

	private SlideMenu	slideMenu;
	private boolean		mTwoPane;

	@Override
	public void onBackPressed() {
		if (this.slideMenu.isShowing()) {
			this.slideMenu.hide();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_cloud_list);

		if (findViewById(R.id.cloud_detail_container) != null) {
			this.mTwoPane = true;
			((CloudListFragment) getSupportFragmentManager().findFragmentById(
					R.id.cloud_list)).setActivateOnItemClick(true);
		}

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		this.slideMenu = new SlideMenu(this);
		this.slideMenu.checkEnabled();
	}

	@Override
	public void onItemSelected(String id) {
		if (this.mTwoPane) {
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				this.slideMenu.show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		// final LoggedUser me = PersistentData.getMe(this);
		//
		// if (me == null) {
		// Intent intent = new Intent();
		// intent.setClass(this, LoginActivity.class);
		// startActivity(intent);
		// }
		//
		// // Get and write the user to the file system
		// Thread t = new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// // data objects
		// Resources res = CloudListActivity.this.getResources();
		// QueryData data = new QueryData();
		// ArrayList<BasicNameValuePair> headers = new
		// ArrayList<BasicNameValuePair>();
		//
		// // Build the headers
		// headers.add(new BasicNameValuePair("X-Auth-Token", me
		// .getClientId()));
		//
		// // Put the data in the query object
		// data.setUrl(res.getString(R.string.cloudsdale_api_base)
		// + res.getString(R.string.cloudsdale_user_endpoint, me.getId()));
		// data.setHeaders(headers);
		//
		// // Get the user from the API
		// UserGetQuery query = new UserGetQuery();
		// LoggedUser u = (LoggedUser) query.execute(data);
		//
		// // Show the progress bar
		// CloudListActivity.this.getSherlock()
		// .setProgressBarIndeterminateVisibility(true);
		//
		// // Wait for the user query to finish
		// while (query.isAlive()) {
		// continue;
		// }
		//
		// // Set the temp user's client id
		// u.setClientId(me.getClientId());
		//
		// // Store all the user's clouds
		// for (Cloud cloud : u.getClouds()) {
		// PersistentData.storeCloud(CloudListActivity.this, cloud);
		// }
		//
		// // Store the updated user
		// PersistentData.StoreMe(CloudListActivity.this, u);
		//
		// // Hid the progress bar
		// CloudListActivity.this.getSherlock()
		// .setProgressBarIndeterminateVisibility(false);
		// }
		// });
		// t.start();
	}
}
