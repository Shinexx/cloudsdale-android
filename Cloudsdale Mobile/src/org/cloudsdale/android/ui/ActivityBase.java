package org.cloudsdale.android.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import org.cloudsdale.android.R;
import org.cloudsdale.android.ui.fragments.SlidingMenuFragment;

public class ActivityBase extends SlidingFragmentActivity {

	protected SlidingMenu	mSlidingMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setup the sliding menu
		mSlidingMenu = getSlidingMenu();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.sliding_menu_host_frame, SlidingMenuFragment.getInstance(),
				getString(R.string.sliding_menu_fragment_tag));
		ft.commit();
		mSlidingMenu.setBehindOffsetRes(R.dimen.actionbar_home_width);

		// Customize actionbar
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
	}

}
