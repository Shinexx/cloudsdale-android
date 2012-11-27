package org.cloudsdale.android.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;

import org.cloudsdale.android.R;

public class AboutActivity extends ActivityBase {
	
	private SlidingMenu mSlidingMenu;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		ActionBar ab = getSupportActionBar();
		ab.setHomeButtonEnabled(true);
		ab.setDisplayHomeAsUpEnabled(true);
		setBehindContentView(R.layout.fragment_sliding_menu_host);
		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setBehindOffsetRes(R.dimen.actionbar_home_width);
	}
	
	@Override
	protected void onPause() {
		finish();
		super.onPause();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if (mSlidingMenu.isBehindShowing()) {
					mSlidingMenu.showAbove();
				} else {
					mSlidingMenu.showBehind();
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
