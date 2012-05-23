package org.cloudsdale.android.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.cloudsdale.android.CloudsdaleMobileApplication;
import org.cloudsdale.android.R;
import org.cloudsdale.android.authentication.CloudsdaleAsyncAuth;
import org.cloudsdale.android.authentication.LoginBundle;
import org.cloudsdale.android.authentication.OAuthBundle;
import org.cloudsdale.android.authentication.Provider;
import org.cloudsdale.android.models.FacebookResponse;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Controller for the login view
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class LoginViewActivity extends SherlockFragmentActivity {
	public static final String	TAG						= "Cloudsdale LoginViewActivity";
	public static final int		FACEBOOK_ACTIVITY_CODE	= 10298;

	private Gson				gson;

	TabHost						mTabHost;
	ViewPager					mViewPager;
	TabsAdapter					mTabsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// SET THEMES HERE
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login_view);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		mViewPager = (ViewPager) findViewById(R.id.pager);

		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

		mTabsAdapter.addTab(mTabHost.newTabSpec("login").setIndicator("Login"),
				LoginFragment.class, null);
		mTabsAdapter.addTab(
				mTabHost.newTabSpec("register").setIndicator("Register"),
				RegisterAccountFragment.class, null);

		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
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

	private void authFromFacebook() {
		if (LoginFragment.fb == null) {
			LoginFragment.fb = new Facebook(
					getString(R.string.facebook_api_token));
		}
		Log.d(TAG, "Engaging FB Auth");
		FacebookRunner runner = new FacebookRunner(LoginFragment.fb);
		runner.request("/me?fields=id", new FbListener());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == FACEBOOK_ACTIVITY_CODE) {
			authFromFacebook();
		}
	}

	private void executeFbAuth(LoginBundle login) {
		Looper.prepare();
		new Auth().execute(login);
	}

	private class FacebookRunner extends AsyncFacebookRunner {

		public FacebookRunner(Facebook fb) {
			super(fb);
		}
	}

	private class FbListener implements AsyncFacebookRunner.RequestListener {

		@Override
		public void onComplete(String response, Object state) {
			String me = response;
			gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
					.create();
			FacebookResponse fbRes = gson.fromJson(me, FacebookResponse.class);
			OAuthBundle oAuth = new OAuthBundle(Provider.FACEBOOK,
					fbRes.getId(), LoginFragment.fb.getAccessToken());
			LoginBundle login = new LoginBundle(null, null,
					getString(R.string.cloudsdale_api_url) + "sessions",
					getString(R.string.cloudsdale_auth_token), oAuth);
			executeFbAuth(login);
		}

		@Override
		public void onIOException(IOException e, Object state) {
			Log.e(TAG, "IO Exception: " + e.getMessage());
			Toast.makeText(LoginViewActivity.this,
					"There was an error logging in, please try again",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
			Log.e(TAG, "FileNotFound Exception: " + e.getMessage());
			Toast.makeText(LoginViewActivity.this,
					"There was an error logging in, please try again",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object state) {
			Log.e(TAG, "MalformedUrl Exception: " + e.getMessage());
			Toast.makeText(LoginViewActivity.this,
					"There was an error logging in, please try again",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {
			Log.e(TAG, "FB Error: " + e.getMessage());
			Toast.makeText(LoginViewActivity.this,
					"There was an error logging in, please try again",
					Toast.LENGTH_LONG).show();
		}

	}

	private class Auth extends CloudsdaleAsyncAuth {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// dialog = new ProgressDialog(LoginViewActivity.this);
			// dialog.setMessage(CloudsdaleMobileApplication.getContext()
			// .getString(R.string.login_dialog_wait_string));
			// dialog.setIndeterminate(true);
			// dialog.setCancelable(false);
			// dialog.show();
		}
	}
}
