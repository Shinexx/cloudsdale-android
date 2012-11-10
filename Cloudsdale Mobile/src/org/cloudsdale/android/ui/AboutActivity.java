package org.cloudsdale.android.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.slidingmenu.lib.app.SlidingFragmentActivity;

import org.cloudsdale.android.R;

public class AboutActivity extends SlidingFragmentActivity {

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		setBehindContentView(R.layout.fragment_sliding_menu_host);
	}
	
	@Override
	protected void onPause() {
		finish();
		super.onPause();
	}

}
