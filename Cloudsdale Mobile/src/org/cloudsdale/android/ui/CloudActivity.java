package org.cloudsdale.android.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.faye.FayeMessageHandler;
import org.cloudsdale.android.managers.FayeManager;
import org.cloudsdale.android.managers.UserManager;
import org.cloudsdale.android.models.CloudsdaleFayeMessage;
import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.api.Drop;
import org.cloudsdale.android.ui.fragments.ChatFragment;
import org.cloudsdale.android.ui.fragments.DropFragment;
import org.cloudsdale.android.ui.fragments.OnlineListFragment;

import java.util.ArrayList;

public class CloudActivity extends SlidingFragmentActivity implements
		FayeMessageHandler {

	private static final int	ONLINE_INDEX	= 0;
	private static final int	DROP_INDEX		= 1;
	private static final int	CHAT_INDEX		= 2;

	private String				mCloudShowingId;
	private Cloud				mCloudShowing;
	private TabHost				mTabHost;
	private ViewPager			mViewPager;
	private TabsAdapter			mTabsAdapter;
	private SlidingMenu			mSlidingMenu;
	private boolean				mDualView;
	private ChatFragment		mChatFrag;
	private DropFragment		mDropFrag;
	private OnlineListFragment	mOnlineFrag;
	private int					mDropFragIndex;
	private int					mOnlineFragIndex;
	private ProgressDialog		sProgressDialog;
	private FrameLayout			mFragmentFrame;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setup the view
		setContentView(R.layout.activity_cloud_view);
		setBehindContentView(R.layout.fragment_sliding_menu_host);

		// Setup the sliding menu
		mSlidingMenu = getSlidingMenu();

		// Customize actionbar
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);

		// Get the tab host
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

		// Check for the frame, see if we're in tablet mode
		mFragmentFrame = (FrameLayout) findViewById(R.id.cloud_fragment_frame);
		mDualView = mFragmentFrame != null
				&& mFragmentFrame.getVisibility() == View.VISIBLE;

		// Get the cloud that's showing and setup the fragments
		GetShowingCloud();
		setupFragments();

		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		} else {
			if (!mDualView) {
				mTabHost.setCurrentTabByTag("chat");
			} else {
				mTabHost.setCurrentTabByTag("drops");
			}
		}
	}

	private void setupFragments() {

		// Add the remaining fragments
		mTabsAdapter.addTab(mTabHost.newTabSpec("online")
				.setIndicator("Online"), OnlineListFragment.class, null);
		mTabsAdapter.addTab(mTabHost.newTabSpec("drops").setIndicator("Drops"),
				DropFragment.class, null);

		if (!mDualView) {
			mDropFragIndex = DROP_INDEX;
			mOnlineFragIndex = ONLINE_INDEX;
		} else {
			mDropFragIndex = DROP_INDEX;
			mOnlineFragIndex = ONLINE_INDEX;
		}
		mDropFrag = (DropFragment) mTabsAdapter.getItem(mDropFragIndex);
		mOnlineFrag = (OnlineListFragment) mTabsAdapter
				.getItem(mOnlineFragIndex);

		// If we're on a phone, add chat to the pager
		// Else, put it in the dualView frame
		if (!mDualView) {
			mTabsAdapter.addTab(mTabHost.newTabSpec("chat")
					.setIndicator("Chat"), ChatFragment.class, null);
			mChatFrag = (ChatFragment) mTabsAdapter.getItem(CHAT_INDEX);
		} else {
			mChatFrag = new ChatFragment();
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.add(R.id.cloud_fragment_frame, mChatFrag);
			ft.commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!FayeManager.isFayeConnected()) {
			showProgressDialog();
			FayeManager.bindFaye();
			new Thread() {
				public void run() {
					while (!FayeManager.isFayeConnected()) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					hideProgressDialog();
				};
			};
		}
		FayeManager.subscribeToMessages(this);
	}

	@Override
	protected void onPause() {
		FayeManager.unsubscribeToMessages(this);
		super.onPause();
	}

	private void showProgressDialog() {
		sProgressDialog = new ProgressDialog(this);
		sProgressDialog.setIndeterminate(true);
		sProgressDialog.setTitle("Cloudsdale is connecting");
		sProgressDialog.setMessage("Please wait...");
		sProgressDialog.setCancelable(false);
		sProgressDialog.show();
	}

	private void hideProgressDialog() {
		if (sProgressDialog != null && sProgressDialog.isShowing()) {
			sProgressDialog.cancel();
		}
		sProgressDialog = null;
	}

	private void GetShowingCloud() {
		Intent intent = getIntent();
		mCloudShowingId = intent.getExtras().getString("cloudId");
		mCloudShowing = UserManager.getCloudFromUser(
				UserManager.getLoggedInUser(), mCloudShowingId);
		Cloudsdale.setShowingCloud(mCloudShowingId);
		getSherlock().setTitle(mCloudShowing.getName());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if (mSlidingMenu.isBehindShowing()) {
					mSlidingMenu.showAbove();
				} else {
					mSlidingMenu.showBehind();
				}
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void handleMessage(final CloudsdaleFayeMessage message) {
		String channel = message.getChannel().substring(1).split("/")[1];
		if (channel.equals(mCloudShowingId)) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mChatFrag.addMessage(message.getData());
					if (message.getData().getDrops() != null
							&& message.getData().getDrops().length > 0) {
						for (Drop drop : message.getData().getDrops()) {
							mDropFrag.addDrop(drop);
						}
					}
				}
			});
		}
	}

	/**
	 * This is a helper class that implements the management of tabs and all
	 * details of connecting a ViewPager with associated TabHost. It relies on a
	 * trick. Normally a tab host has a simple API for supplying a View or
	 * Intent that each tab will show. This is not sufficient for switching
	 * between pages. So instead we make the content part of the tab host 0dp
	 * high (it is not shown) and the TabsAdapter supplies its own dummy view to
	 * show as the tab content. It listens to changes in tabs, and takes care of
	 * switch to the correct paged in the ViewPager whenever the selected tab
	 * changes.
	 */
	public static class TabsAdapter extends FragmentPagerAdapter implements
			TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
		private final Context				mContext;
		private final TabHost				mTabHost;
		private final ViewPager				mViewPager;
		private final ArrayList<TabInfo>	mTabs	= new ArrayList<TabInfo>();

		static final class TabInfo {
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
}
