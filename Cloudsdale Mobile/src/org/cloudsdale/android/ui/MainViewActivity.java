package org.cloudsdale.android.ui;

import java.util.ArrayList;

import org.cloudsdale.android.R;
import org.cloudsdale.android.ui.fragments.CloudListFragment;
import org.cloudsdale.android.ui.fragments.HomeFragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.b3rwynmobile.fayeclient.FayeClient;
import com.b3rwynmobile.fayeclient.FayeListener;
import com.b3rwynmobile.fayeclient.FayeService;
import com.b3rwynmobile.fayeclient.FayeService.FayeBinder;

/**
 * Activity controller for the primary app view
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class MainViewActivity extends SherlockFragmentActivity {

	private TabHost			tabHost;
	private ViewPager		viewPager;
	private TabsAdapter		tabsAdapter;
	private FayeConnection	fayeConnection;
	private FayeClient		fayeClient;
	private FayeListener	fayeListener;

	private boolean			serviceBound;

	/**
	 * Life cycle method for the creation of the activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set the layout
		setContentView(R.layout.main_view);

		// Setup the tabs
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();

		viewPager = (ViewPager) findViewById(R.id.pager);

		tabsAdapter = new TabsAdapter(this, tabHost, viewPager);

		// Add the fragments to the tab
		tabsAdapter.addTab(tabHost.newTabSpec("home").setIndicator("Home"),
				HomeFragment.class, null);
		tabsAdapter.addTab(tabHost.newTabSpec("clouds").setIndicator("Clouds"),
				CloudListFragment.class, null);

		// Create an instance state if it doesn't exist
		if (savedInstanceState != null) {
			tabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}

	}

	/**
	 * Life cycle method for view starting, having already been created
	 */
	@Override
	protected void onStart() {
		super.onStart();

		// Create the Faye Connection
		fayeConnection = new FayeConnection();

		// Start Faye
		bindService(new Intent(this, FayeService.class), fayeConnection,
				Context.BIND_AUTO_CREATE);
	}

	/**
	 * Save the instance of the view on exit
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", tabHost.getCurrentTabTag());
	}

	/**
	 * Life cycle method to handle killing Faye when the app is destroyed
	 */
	@Override
	public boolean isFinishing() {
		stopService(new Intent(this, FayeService.class));
		return super.isFinishing();
	}

	/**
	 * Returns the status of the bound service
	 * 
	 * @return Whether the service is bound or not
	 */
	public boolean isServiceBound() {
		return serviceBound;
	}

	/**
	 * This is a helper class that implements the management of tabs and all
	 * details of connecting a ViewPager with associated TabHost. It relies on a
	 * trick. Normally a tab host has a simple API for supplying a View or
	 * Intent that each tab will show. This is not sufficient for switching
	 * between pages. So instead we make the content part of the tab host
	 * 0dp high (it is not shown) and the TabsAdapter supplies its own dummy
	 * view to show as the tab content. It listens to changes in tabs, and takes
	 * care of switch to the correct paged in the ViewPager whenever the
	 * selected
	 * tab changes.
	 */
	public static class TabsAdapter extends FragmentPagerAdapter implements
			TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
		private final Context				mContext;
		private final TabHost				mTabHost;
		private final ViewPager				mViewPager;
		private final ArrayList<TabInfo>	mTabs	= new ArrayList<TabInfo>();

		static final class TabInfo {
			@SuppressWarnings("unused")
			private final String	tag;
			private final Class<?>	clss;
			private final Bundle	args;

			TabInfo(String _tag, Class<?> _class, Bundle _args) {
				tag = _tag;
				clss = _class;
				args = _args;
			}
		}

		static class DummyTabFactory implements TabHost.TabContentFactory {
			private final Context	mContext;

			public DummyTabFactory(Context context) {
				mContext = context;
			}

			@Override
			public View createTabContent(String tag) {
				View v = new View(mContext);
				v.setMinimumWidth(0);
				v.setMinimumHeight(0);
				return v;
			}
		}

		public TabsAdapter(FragmentActivity activity, TabHost tabHost,
				ViewPager pager) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mTabHost = tabHost;
			mViewPager = pager;
			mTabHost.setOnTabChangedListener(this);
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
			tabSpec.setContent(new DummyTabFactory(mContext));
			String tag = tabSpec.getTag();

			TabInfo info = new TabInfo(tag, clss, args);
			mTabs.add(info);
			mTabHost.addTab(tabSpec);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(),
					info.args);
		}

		@Override
		public void onTabChanged(String tabId) {
			int position = mTabHost.getCurrentTab();
			mViewPager.setCurrentItem(position);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			// Unfortunately when TabHost changes the current tab, it kindly
			// also takes care of putting focus on it when not in touch mode.
			// The jerk.
			// This hack tries to prevent this from pulling focus out of our
			// ViewPager.
			TabWidget widget = mTabHost.getTabWidget();
			int oldFocusability = widget.getDescendantFocusability();
			widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			mTabHost.setCurrentTab(position);
			widget.setDescendantFocusability(oldFocusability);
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}
	}

	private class FayeConnection implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName className, IBinder binder) {
			// Cast the binder so it's usable
			FayeBinder _binder = (FayeBinder) binder;

			// Bind the faye objects
			MainViewActivity.this.fayeClient = _binder.getFayeClient();
			MainViewActivity.this.fayeListener = MainViewActivity.this.fayeClient
					.getFayeListener();

			// Set the bound status
			MainViewActivity.this.serviceBound = true;
			
			// Open the socket connection
			MainViewActivity.this.fayeClient.openSocketConnection();
			
			// Open the push connection
			MainViewActivity.this.fayeClient.connectFaye();
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			MainViewActivity.this.serviceBound = false;
		}

	}
}
