package org.cloudsdale.android.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class MainViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
		
		TextView tv = new TextView(this);
		SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
		String me = sharedPrefs.getString("me", "");
		tv.setText(me);
		addContentView(tv, null);
	}
}
