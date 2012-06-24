package org.cloudsdale.android.ui;

import org.cloudsdale.android.R;
import org.cloudsdale.android.ui.fragments.HomeFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class MainViewActivity extends SherlockFragmentActivity implements
		ActionBar.OnNavigationListener {

	private String[]	mLocations;

	/**
	 * Life cycle method for the creation of the activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get the locations available
		mLocations = getResources().getStringArray(R.array.view_locations);

		// Create the array adapter for the locations
		Context context = getSupportActionBar().getThemedContext();
		ArrayAdapter<CharSequence> list = ArrayAdapter
				.createFromResource(context, R.array.view_locations,
						R.layout.sherlock_spinner_item);
		list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

		// Set the navigation mode to the list dropdown mode
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getSupportActionBar().setListNavigationCallbacks(list, this);

		// Set the content view
		setContentView(R.layout.main_view);

		// Setup the home fragment if no previous state to restore
		if (savedInstanceState == null) {
			HomeFragment home = new HomeFragment();
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.add(R.id.embedded_view, home);
			ft.commit();
		}
	}

	/**
	 * Callback method to handle list navigation. Takes the selected item and
	 * displays the proper fragment for it
	 */
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		return false;
	}

}
