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

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.PersistentData;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.api_models.User;

public class SlideMenu {

	public static class SlideMenuAdapter extends
			ArrayAdapter<SlideMenu.SlideMenuAdapter.MenuDesc> {
		static class MenuDesc {
			public int		icon;
			public String	label;
		}

		class MenuItem {
			public TextView		label;
			public ImageView	icon;
		}

		Activity								act;

		SlideMenu.SlideMenuAdapter.MenuDesc[]	items;

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
				LayoutInflater inflater = this.act.getLayoutInflater();
				rowView = inflater.inflate(R.layout.menu_list_item, null);
				MenuItem viewHolder = new MenuItem();
				viewHolder.label = (TextView) rowView
						.findViewById(R.id.menu_label);
				viewHolder.icon = (ImageView) rowView
						.findViewById(R.id.menu_icon);
				rowView.setTag(viewHolder);
			}

			MenuItem holder = (MenuItem) rowView.getTag();
			String s = this.items[position].label;
			holder.label.setText(s);
			holder.icon.setImageResource(this.items[position].icon);

			return rowView;
		}
	}

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
		if (SlideMenu.menuShown) {
			this.show(false);
		}
	}

	public void fill() {
		ListView list = (ListView) this.act.findViewById(R.id.slide_menu_list);
		SlideMenuAdapter.MenuDesc[] items = new SlideMenuAdapter.MenuDesc[2];
		setupStaticMenuOptions(items);
		SlideMenuAdapter adap = new SlideMenuAdapter(this.act, items);
		list.setAdapter(adap);
	}

	public void hide() {
		TranslateAnimation ta = new TranslateAnimation(0, -SlideMenu.menuSize,
				0, 0);
		ta.setDuration(500);
		SlideMenu.menu.startAnimation(ta);
		SlideMenu.parent.removeView(SlideMenu.menu);

		TranslateAnimation tra = new TranslateAnimation(SlideMenu.menuSize, 0,
				0, 0);
		tra.setDuration(500);
		SlideMenu.content.startAnimation(tra);
		FrameLayout.LayoutParams parm = (FrameLayout.LayoutParams) SlideMenu.content
				.getLayoutParams();
		parm.setMargins(0, 0, 0, 0);
		SlideMenu.content.setLayoutParams(parm);
		Cloudsdale.enableDisableViewGroup((LinearLayout) SlideMenu.parent
				.findViewById(android.R.id.content).getParent(), true);
		// ((ExtendedViewPager) act.findViewById(R.id.viewpager))
		// .setPagingEnabled(true);
		// ((ExtendedPagerTabStrip) act.findViewById(R.id.viewpager_tabs))
		// .setNavEnabled(true);
		SlideMenu.menuShown = false;
	}

	public boolean isShowing() {
		return SlideMenu.menuShown;
	}

	private void setupStaticMenuOptions(SlideMenuAdapter.MenuDesc[] items) {
		items[0] = new SlideMenuAdapter.MenuDesc();
		items[0].icon = R.drawable.ic_launcher;
		items[0].label = "Home";
		items[1] = new SlideMenuAdapter.MenuDesc();
		items[1].icon = R.drawable.ic_launcher;
		items[1].label = "Settings";
	}

	public void show() {
		// get the height of the status bar
		if (SlideMenu.statusHeight == 0) {
			Rect rectangle = new Rect();
			Window window = this.act.getWindow();
			window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
			SlideMenu.statusHeight = rectangle.top;
		}

		this.show(true);
	}

	public void show(boolean animate) {
		SlideMenu.menuSize = Cloudsdale.dpToPx(250, this.act);
		SlideMenu.content = ((LinearLayout) this.act.findViewById(
				android.R.id.content).getParent());
		FrameLayout.LayoutParams parm = (FrameLayout.LayoutParams) SlideMenu.content
				.getLayoutParams();
		parm.setMargins(SlideMenu.menuSize, 0, -SlideMenu.menuSize, 0);
		SlideMenu.content.setLayoutParams(parm);

		// animation for smooth slide-out
		TranslateAnimation ta = new TranslateAnimation(-SlideMenu.menuSize, 0,
				0, 0);
		ta.setDuration(500);
		if (animate) {
			SlideMenu.content.startAnimation(ta);
		}
		SlideMenu.parent = (FrameLayout) SlideMenu.content.getParent();
		LayoutInflater inflater = (LayoutInflater) this.act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		SlideMenu.menu = inflater.inflate(R.layout.menu, null);

		// Setup the user tile
		User user = PersistentData.getMe(act.getBaseContext());
		ImageView userIcon = (ImageView) menu
				.findViewById(R.id.slide_menu_user_icon);
		UrlImageViewHelper.setUrlDrawable(userIcon, user.getAvatar().getNormal(),
				R.drawable.unknown_user, 1000 * 60 * 60);
		TextView username = (TextView) menu
				.findViewById(R.id.slide_menu_username_label);
		username.setText(user.getName());

		FrameLayout.LayoutParams lays = new FrameLayout.LayoutParams(-1, -1, 1);
		lays.setMargins(0, SlideMenu.statusHeight, 0, 0);
		SlideMenu.menu.setLayoutParams(lays);
		SlideMenu.parent.addView(SlideMenu.menu);
		ListView list = (ListView) this.act.findViewById(R.id.slide_menu_list);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// handle your menu-click
			}
		});
		if (animate) {
			SlideMenu.menu.startAnimation(ta);
		}
		
		SlideMenu.menu.findViewById(R.id.overlay).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						SlideMenu.this.hide();
					}
				});
		Cloudsdale.enableDisableViewGroup((LinearLayout) SlideMenu.parent
				.findViewById(android.R.id.content).getParent(), false);
		// ((ExtendedViewPager) act.findViewById(R.id.viewpager))
		// .setPagingEnabled(false);
		// ((ExtendedPagerTabStrip) act.findViewById(R.id.viewpager_tabs))
		// .setNavEnabled(false);
		SlideMenu.menuShown = true;
		fill();
	}

}
