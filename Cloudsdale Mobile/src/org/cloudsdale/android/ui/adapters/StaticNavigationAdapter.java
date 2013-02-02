package org.cloudsdale.android.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import org.cloudsdale.android.R;
import org.cloudsdale.android.ui.StaticNavigation;

public class StaticNavigationAdapter extends BaseAdapter {

	private Context	context;

	public StaticNavigationAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return StaticNavigation.values().length;
	}

	@Override
	public StaticNavigation getItem(int position) {
		return StaticNavigation.values()[position];
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getTextId().hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		AQuery aq = new AQuery(context);
		View view = aq.inflate(convertView, R.layout.widget_static_navigation,
				parent);

		((ImageView) view.findViewById(R.id.static_navigation_icon))
				.setImageResource(getItem(position).getResId());
		((TextView) view.findViewById(R.id.static_navigation_label))
				.setText(getItem(position).getDisplayName());

		return view;
	}
}
