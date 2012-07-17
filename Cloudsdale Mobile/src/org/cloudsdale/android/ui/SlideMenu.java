package org.cloudsdale.android.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;

public class SlideMenu {

	private static boolean		menuShown		= false;
	private static View			menu;
	private static LinearLayout	content;
	private static FrameLayout	parent;
	private static int			menuSize;
	private static int			statusHeight	= 0;
	private Activity			act;

	public SlideMenu(Activity act) {
		this.act = act;
	}

	public void checkEnabled() {
		if (menuShown) {
			this.show(false);
		}
	}
	
	public boolean isShowing() {
		return menuShown;
	}

	public void show() {
		// get the height of the status bar
		if (statusHeight == 0) {
			Rect rectangle = new Rect();
			Window window = act.getWindow();
			window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
			statusHeight = rectangle.top;
		}

		this.show(true);
	}

	public void show(boolean animate) {
		menuSize = Cloudsdale.dpToPx(250, act);
		content = ((LinearLayout) act.findViewById(android.R.id.content)
				.getParent());
		FrameLayout.LayoutParams parm = (FrameLayout.LayoutParams) content
				.getLayoutParams();
		parm.setMargins(menuSize, 0, -menuSize, 0);
		content.setLayoutParams(parm);
		// animation for smooth slide-out
		TranslateAnimation ta = new TranslateAnimation(-menuSize, 0, 0, 0);
		ta.setDuration(500);
		if (animate) content.startAnimation(ta);
		parent = (FrameLayout) content.getParent();
		LayoutInflater inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		menu = inflater.inflate(R.layout.menu, null);
		FrameLayout.LayoutParams lays = new FrameLayout.LayoutParams(-1, -1, 3);
		lays.setMargins(0, statusHeight, 0, 0);
		menu.setLayoutParams(lays);
		parent.addView(menu);
		ListView list = (ListView) act.findViewById(R.id.slide_menu_list);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// handle your menu-click
			}
		});
		if (animate) menu.startAnimation(ta);
		menu.findViewById(R.id.overlay).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						SlideMenu.this.hide();
					}
				});
		Cloudsdale.enableDisableViewGroup(
				(LinearLayout) parent.findViewById(android.R.id.content)
						.getParent(), false);
		// ((ExtendedViewPager) act.findViewById(R.id.viewpager))
		// .setPagingEnabled(false);
		// ((ExtendedPagerTabStrip) act.findViewById(R.id.viewpager_tabs))
		// .setNavEnabled(false);
		menuShown = true;
		this.fill();
	}

	public void fill() {
		ListView list = (ListView) act.findViewById(R.id.slide_menu_list);
		SlideMenuAdapter.MenuDesc[] items = new SlideMenuAdapter.MenuDesc[2];
		setupStaticMenuOptions(items);
		SlideMenuAdapter adap = new SlideMenuAdapter(act, items);
		list.setAdapter(adap);
	}

	private void setupStaticMenuOptions(SlideMenuAdapter.MenuDesc[] items) {
		items[0] = new SlideMenuAdapter.MenuDesc();
		items[0].icon = R.drawable.ic_launcher;
		items[0].label = "Home";
		items[1] = new SlideMenuAdapter.MenuDesc();
		items[1].icon = R.drawable.ic_launcher;
		items[1].label = "Settings";
	}

	public void hide() {
		TranslateAnimation ta = new TranslateAnimation(0, -menuSize, 0, 0);
		ta.setDuration(500);
		menu.startAnimation(ta);
		parent.removeView(menu);

		TranslateAnimation tra = new TranslateAnimation(menuSize, 0, 0, 0);
		tra.setDuration(500);
		content.startAnimation(tra);
		FrameLayout.LayoutParams parm = (FrameLayout.LayoutParams) content
				.getLayoutParams();
		parm.setMargins(0, 0, 0, 0);
		content.setLayoutParams(parm);
		 Cloudsdale.enableDisableViewGroup(
		 (LinearLayout) parent.findViewById(android.R.id.content)
		 .getParent(), true);
		// ((ExtendedViewPager) act.findViewById(R.id.viewpager))
		// .setPagingEnabled(true);
		// ((ExtendedPagerTabStrip) act.findViewById(R.id.viewpager_tabs))
		// .setNavEnabled(true);
		menuShown = false;
	}

	public static class SlideMenuAdapter extends
			ArrayAdapter<SlideMenu.SlideMenuAdapter.MenuDesc> {
		Activity								act;
		SlideMenu.SlideMenuAdapter.MenuDesc[]	items;

		class MenuItem {
			public TextView		label;
			public ImageView	icon;
		}

		static class MenuDesc {
			public int		icon;
			public String	label;
		}

		public SlideMenuAdapter(Activity act,
				SlideMenu.SlideMenuAdapter.MenuDesc[] items) {
			super(act, R.id.menu_label, items);
			this.act = act;
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView = convertView;
			if (rowView == null) {
				LayoutInflater inflater = act.getLayoutInflater();
				rowView = inflater.inflate(R.layout.menu_list_item, null);
				MenuItem viewHolder = new MenuItem();
				viewHolder.label = (TextView) rowView
						.findViewById(R.id.menu_label);
				viewHolder.icon = (ImageView) rowView
						.findViewById(R.id.menu_icon);
				rowView.setTag(viewHolder);
			}

			MenuItem holder = (MenuItem) rowView.getTag();
			String s = items[position].label;
			holder.label.setText(s);
			holder.icon.setImageResource(items[position].icon);

			return rowView;
		}
	}

}
