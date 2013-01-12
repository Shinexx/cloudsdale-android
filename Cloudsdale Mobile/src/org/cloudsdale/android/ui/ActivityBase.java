package org.cloudsdale.android.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;

/**
 * Base class for all Cloudsdale classes that use sliding menu navigation (e.g.
 * most of them) Copyright (c) 2012 Cloudsdale.org
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class ActivityBase extends SlidingFragmentActivity {

	protected SlidingMenu	mSlidingMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set the common behind view
		setBehindContentView(R.layout.fragment_sliding_menu_host);

		// Setup the sliding menu
		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setBehindOffsetRes(R.dimen.actionbar_home_width);

		// Customize actionbar
		ActionBar actionbar = getSherlock().getActionBar();
		actionbar.setHomeButtonEnabled(true);
		actionbar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				mSlidingMenu.toggle();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		// Attach the sliding menu fragment
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.sliding_menu_host_frame, Cloudsdale.getSlidingMenu(),
				getString(R.string.sliding_menu_fragment_tag));
		ft.commit();

		super.onResume();
	}

	@Override
	protected void onPause() {
		// Remove the fragment so it can show up elsewhere
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.remove(Cloudsdale.getSlidingMenu());
		ft.commit();

		super.onPause();
	}

}
