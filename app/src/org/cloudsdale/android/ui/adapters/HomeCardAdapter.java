package org.cloudsdale.android.ui.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import org.cloudsdale.android.R;
import org.cloudsdale.android.models.api.User;

public class HomeCardAdapter extends ArrayAdapter<User> {

	public HomeCardAdapter(Context context) {
		super(context, R.layout.widget_home_account_card);
	}

}
