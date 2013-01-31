package org.cloudsdale.android.ui;

import android.os.Bundle;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import org.cloudsdale.android.R;

/**
 * Base activity to do core setup. <br/>
 * Copyright (c) 2012 Cloudsdale.org
 * 
 * @author Jamison Greeley (berwyn.codeweaver@gmail.com)
 * 
 */
public abstract class CloudsdaleActivity extends SlidingFragmentActivity {
	
	protected SlidingMenu slidingMenu;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setBehindContentView(R.layout.activity_home);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		slidingMenu = getSlidingMenu();
		slidingMenu.setSlidingEnabled(true);
		
		super.onCreate(savedInstanceState);
	}

}
