package org.cloudsdale.android.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import org.cloudsdale.android.R;
import org.cloudsdale.android.ui.fragments.HomeFragment;
import org.cloudsdale.android.ui.fragments.SlidingMenuFragment;

/**
 * Base activity to do core setup. <br/>
 * Copyright (c) 2012 Cloudsdale.org
 * 
 * @author Jamison Greeley (berwyn.codeweaver@gmail.com)
 * 
 */
public class CloudsdaleActivity extends SlidingFragmentActivity {

	private static final String	SAVED_FRAGMENT_KEY	= "savedFragment";

	private SlidingMenu			slidingMenu;
	private SlidingMenuFragment	slidingFragment;
	private boolean				isOnTablet;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		isOnTablet = findViewById(R.id.menu_frame) != null;

		if (isOnTablet) {
			setBehindContentView(new View(this));
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
			slidingMenu = getSlidingMenu();
			slidingMenu.setSlidingEnabled(false);
			slidingFragment = new SlidingMenuFragment();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.menu_frame, slidingFragment).commit();
		} else {
			setBehindContentView(R.layout.widget_sliding_menu);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			setSlidingActionBarEnabled(true);
			showContent();
			slidingMenu = getSlidingMenu();
			slidingMenu.setBehindOffsetRes(R.dimen.actionbar_home_width);
			slidingMenu.setSlidingEnabled(true);
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			slidingMenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
			((ListView) findViewById(android.R.id.list))
					.setAdapter(SlidingMenuFragment.generateAdapter(this));
		}

		if (savedInstanceState != null
				&& savedInstanceState.containsKey(SAVED_FRAGMENT_KEY)) {

		} else {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, new HomeFragment()).commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if (!isOnTablet) {
					toggle();
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);

		}
	}

}
