package org.cloudsdale.android.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import org.cloudsdale.android.R;

public class AboutActivity extends ActivityBase {

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}

	@Override
	protected void onPause() {
		finish();
		super.onPause();
	}

}
