package org.cloudsdale.android.ui;

import java.util.ArrayList;

import org.cloudsdale.android.R;
import org.cloudsdale.android.authentication.CloudsdaleAsyncAuth;
import org.cloudsdale.android.authentication.LoginBundle;
import org.cloudsdale.android.authentication.OAuthBundle;
import org.cloudsdale.android.authentication.Provider;
import org.cloudsdale.android.logic.PersistentData;
import org.cloudsdale.android.models.User;
import org.cloudsdale.android.ui.fragments.LoginFragment;
import org.cloudsdale.android.ui.fragments.RegisterAccountFragment;

import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * Controller for the login view
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class LoginViewActivity extends SherlockFragmentActivity {
	public static final String	TAG						= "Cloudsdale LoginViewActivity";
	public static final int		FACEBOOK_ACTIVITY_CODE	= 10298;

	TabHost						mTabHost;
	ViewPager					mViewPager;
	TabsAdapter					mTabsAdapter;

	private ProgressDialog		progress;
	private Auth				auth;

	/**
	 * Lifetime method for the creation of the controller
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// SET THEMES HERE
		super.onCreate(savedInstanceState);

		// Bind the tab host
		setContentView(R.layout.login_view);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		// Bind the pager
		mViewPager = (ViewPager) findViewById(R.id.pager);

		// Build the tab adapter
		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

		// Add the tab fragments
		mTabsAdapter.addTab(mTabHost.newTabSpec("login").setIndicator("Login"),
				LoginFragment.class, null);
		mTabsAdapter.addTab(
				mTabHost.newTabSpec("register").setIndicator("Register"),
				RegisterAccountFragment.class, null);

		// Save the current tab if needed
		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
	}

	/**
	 * Keep track of the current tab
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
	}

	/**
	 * Handle results sent from an activity started for an intent
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		LoginFragment.fb.authorizeCallback(requestCode, resultCode, data);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Uri uri = intent.getData();
		try {
			String verifier = uri.getQueryParameter("oauth_verifier");
			AccessToken accessToken = LoginFragment.twitter
					.getOAuthAccessToken(LoginFragment.twitterRequestToken,
							verifier);
			String token = accessToken.getToken();
			String secret = accessToken.getTokenSecret();
			OAuthBundle auth = new OAuthBundle(Provider.TWITTER,
					String.valueOf(LoginFragment.twitter.getId()),
					getString(R.string.cloudsdale_auth_token));
			LoginBundle bundle = new LoginBundle(null, null,
					getString(R.string.cloudsdale_api_url) + "sessions",
					getString(R.string.cloudsdale_auth_token), auth);
			new CloudsdaleAsyncAuth().execute(bundle);
		} catch (TwitterException e) {
			Toast.makeText(this,
					"There was an error with Twitter, please try again",
					Toast.LENGTH_LONG);
		}
	}

	public Auth getAuth() {
		auth = new Auth();
		return auth;
	}

	public void showDialog() {
		progress = ProgressDialog.show(this, "",
				getString(R.string.login_dialog_wait_string));
	}

	public void cancelDialog() {
		if (progress.isShowing()) {
			progress.dismiss();
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
	 * changes. -- From the ActionBar Sherlock demo
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

	public class Auth extends CloudsdaleAsyncAuth {

		@Override
		protected void onCancelled(User result) {
			super.onCancelled(result);
			Toast.makeText(LoginViewActivity.this,
					"There was an error logging in, please try again",
					Toast.LENGTH_LONG);

			cancelDialog();
		}

		@Override
		protected void onPostExecute(User result) {
			if (result != null) {
				PersistentData.setMe(result);
			} else {
				Toast.makeText(LoginViewActivity.this,
						"There was an error, please try again",
						Toast.LENGTH_LONG);
			}

			cancelDialog();
		}
	}
}