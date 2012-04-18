package org.cloudsdale.android.ui;

import org.cloudsdale.android.R;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class MainViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);

		//Setup the ActionBar for navigation
		ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.main_view_spinner_categories,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bar.setListNavigationCallbacks(adapter, new OnNavigationListener() {
			
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {
				return false;
			}
		});
	}
}
