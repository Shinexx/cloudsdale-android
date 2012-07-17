package org.cloudsdale.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

import org.cloudsdale.android.R;
import org.cloudsdale.android.ui.fragments.CloudDetailFragment;

public class CloudDetailActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_cloud_detail);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			arguments.putString(CloudDetailFragment.ARG_ITEM_ID, getIntent()
					.getStringExtra(CloudDetailFragment.ARG_ITEM_ID));
			CloudDetailFragment fragment = new CloudDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.cloud_detail_container, fragment).commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			NavUtils.navigateUpTo(this, new Intent(this,
					CloudListActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
