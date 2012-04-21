package org.cloudsdale.android.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MainViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
		
		SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
		String me = sharedPrefs.getString("me", "");
		
		Toast.makeText(this, me, Toast.LENGTH_LONG);
		
		TextView tv = new TextView(this);
		tv.setText(me);
		this.addContentView(tv, new LayoutParams(50, 100));
	}
}
