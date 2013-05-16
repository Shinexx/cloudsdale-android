package org.cloudsdale.android.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.androidquery.AQuery;
import com.bugsense.trace.BugSenseHandler;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.slidingmenu.lib.SlidingMenu;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.DataStore;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.api.Session;
import org.cloudsdale.android.models.api.User;
import org.cloudsdale.android.models.network.Provider;
import org.cloudsdale.android.models.network.SessionResponse;
import org.cloudsdale.android.ui.adapters.CloudAdapter;
import org.cloudsdale.android.ui.adapters.SlidingMenuAdapter;
import org.cloudsdale.android.ui.fragments.AboutFragment_;
import org.cloudsdale.android.ui.fragments.CloudFragment;
import org.cloudsdale.android.ui.fragments.CloudFragment_;
import org.cloudsdale.android.ui.fragments.HomeFragment;
import org.cloudsdale.android.ui.fragments.HomeFragment_;
import org.cloudsdale.android.ui.fragments.LoginFragment_;
import org.cloudsdale.android.ui.fragments.SlidingMenuFragment;
import org.codeweaver.remoteconfiguredhttpclient.RemoteConfigurationListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.val;

/**
 * Base activity to do core setup. <br/>
 * Copyright (c) 2012 Cloudsdale.org
 * 
 * @author Jamison Greeley (berwyn.codeweaver@gmail.com)
 * 
 */
@EActivity(R.layout.activity_base)
public class CloudsdaleActivity extends Activity implements
		SlidingMenuFragment.ISlidingMenuFragmentCallbacks, ActivityCallbacks,
		RemoteConfigurationListener {

	// Suppressing Lint warning - Resource object doesn't exist at compile time,
	// asset IDs do thanks to R.java
	@SuppressLint("ResourceAsColor")
	public static final Style							INFINITE			= new Style.Builder()
																					.setBackgroundColor(
																							R.color.holo_red_light)
																					.setDuration(
																							Style.DURATION_INFINITE)
																					.build();

	private static final String							TAG					= "Cloudsdale Activity";
	private static final String							SAVED_FRAGMENT_KEY	= "savedFragment";

	private HomeFragment_								homeFragment;
	private AboutFragment_								aboutFragment;
	private LoginFragment_								loginFragment;
	private SlidingMenuFragment							slidingFragment;
	private SlidingMenu									slidingMenu;
	private boolean										isOnTablet;
	private AQuery										aQuery;
	private Map<String, SoftReference<CloudFragment_>>	cloudFragments;
	private CloudFragment								currentCloud;
	AtomicBoolean										isShowing			= new AtomicBoolean(
																					false);

	// Injected resources
	@App
	Cloudsdale											cloudsdale;
	@StringRes(R.string.cloudsdale_auth_token)
	String												authToken;
	@StringRes(R.string.sliding_fragment_tag)
	String												slidingFragmentTag;
	@ViewById(R.id.base_config_placeholder)
	View												placeholderView;
	@StringRes(R.string.bugsense_key)
	String												bugsenseApiKey;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BugSenseHandler.initAndStartSession(this, bugsenseApiKey);
		aQuery = new AQuery(this);
		cloudFragments = new HashMap<String, SoftReference<CloudFragment_>>();

		isOnTablet = findViewById(R.id.tablet_menu) != null;
		generateFragments();
		if (!isOnTablet) {
			generateSlidingMenu();
		}

		FragmentManager fm = getSupportFragmentManager();
		slidingFragment = (SlidingMenuFragment) fm
				.findFragmentByTag(slidingFragmentTag);
		slidingFragment.setCallback(this);

		if (savedInstanceState != null
				&& savedInstanceState.containsKey(SAVED_FRAGMENT_KEY)) {
			// TODO replace fragments when we icicle the activity
		} else {
			cloudsdale.configure(this);
		}
	}

	@Override
	protected void onResume() {
		isShowing.set(true);
		super.onResume();
	}

	@Override
	protected void onPause() {
		isShowing.set(false);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		slidingFragment.setCallback(null);
		cloudsdale.stopConfiguration();
		super.onDestroy();
	}

	@Override
	public void listItemClicked(String id, Class<?> clazz) {
		if (!isShowing.get()) return;
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		if (clazz == StaticNavigation.class) {
			Bundle args = new Bundle();
			if (id.equals("home")) {
				ft.replace(R.id.content_frame, homeFragment);
				args.putBoolean(HomeFragment.SHOULD_INFLATE_CARDS_BUNDLE_KEY,
						true);
				homeFragment.setArguments(args);
			} else if (id.equals("about")) {
				ft.replace(R.id.content_frame, aboutFragment);
			}
		} else if (clazz == Cloud.class) {
			replaceCloudFragment(id, ft);
		}
		ft.commit();
	}

	public void displayLoginFailCrouton(String error) {
		if (cloudsdale.isDebuggable() && error != null) {
			Log.e(TAG, error);
		}
		Crouton.showText(CloudsdaleActivity.this,
				"There was an error loading your account",
				CloudsdaleActivity.INFINITE);
	}

	@Override
	@UiThread
	public void onLoginCompleted() {
		if (!isShowing.get()) return;
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.remove(loginFragment);
		ft.add(R.id.content_frame, homeFragment);
		ft.commit();
	}

	@Override
	@UiThread
	public void onLoginFailed() {
		// TODO Auto-generated method stub

	}

	@Override
	@UiThread
	public void onConfigurationFailed(Throwable error, JSONObject response) {
		// TODO Auto-generated method stub

	}

	@Override
	@UiThread
	public void onConfigurationSucceeded(int statusCode,
			JSONObject configuration) {
		if (!isShowing.get()) return;
		aQuery.id(placeholderView).gone();
		if (DataStore.getActiveAccount() == null
				&& DataStore.getAccounts().length <= 0) {
			loginFragment = new LoginFragment_();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, loginFragment).commit();
		} else {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, homeFragment).commit();
			handleSessionRenewal();
		}
	}

	private void generateFragments() {
		if (homeFragment == null) {
			homeFragment = new HomeFragment_();
			homeFragment.setRetainInstance(true);
		}
		if (aboutFragment == null) {
			aboutFragment = new AboutFragment_();
			aboutFragment.setRetainInstance(true);
		}
	}

	private void generateSlidingMenu() {
		getSupportActionBar().setHomeButtonEnabled(true);
		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		slidingMenu.setMenu(R.layout.fragment_sliding_menu);
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
	}

	private void refreshSlidingMenuClouds(User user) {
		if (cloudsdale.isDebuggable()) {
			Log.d(TAG, String.format("Received refresh for user %1s",
					user == null ? "[null]" : user.getName() + " with "
							+ user.getClouds().size() + "clouds"));
		}
		SlidingMenuAdapter adapter = (SlidingMenuAdapter) slidingFragment
				.getListAdapter();
		CloudAdapter cloudsAdapter = adapter.getCloudAdapter();
		val clouds = user.getClouds();
		cloudsAdapter.addCloud(clouds.toArray(new Cloud[clouds.size()]));
	}

	private void handleSessionRenewal() {
		if (DataStore.getActiveAccount() == null) {
			if (cloudsdale.isDebuggable()) {
				Log.d(TAG, "Renewing Session");
			}
			String accountId = DataStore.getAccountIds()[0];
			try {
				cloudsdale.callZephyr().postSession(accountId,
						Provider.CLOUDSDALE, authToken,
						new AsyncHttpClient.JSONObjectCallback() {

							@Override
							public void onCompleted(Exception e,
									AsyncHttpResponse source, JSONObject result) {
								if (e != null) {
									displayLoginFailCrouton(result.toString());
								} else {
									SessionResponse response = cloudsdale
											.getJsonDeserializer().fromJson(
													result.toString(),
													SessionResponse.class);
									Session session = response.getResult();
									DataStore.storeAccount(session);
									cloudsdale.getUserDataStore().store(
											session.getUser());
									homeFragment.inflateHomeCards(session
											.getUser());
									refreshSlidingMenuClouds(session.getUser());
								}
							}

						});
			} catch (JSONException e) {
				displayLoginFailCrouton(e.getMessage());
			}
		} else {
			if (cloudsdale.isDebuggable()) {
				Log.d(TAG, "No session renewal required, inflating home view");
			}
			homeFragment.inflateHomeCards(cloudsdale.getLoggedInUser());
		}
	}

	private void replaceCloudFragment(String id, FragmentTransaction ft) {
		CloudFragment_ replaceFrag;
		if (!cloudFragments.containsKey(id)
				|| cloudFragments.get(id).get() == null) {
			replaceFrag = new CloudFragment_();
			cloudFragments.put(id, new SoftReference<CloudFragment_>(
					replaceFrag));
		} else {
			replaceFrag = cloudFragments.get(id).get();
		}
		if (!replaceFrag.equals(currentCloud)) {
			Bundle args = new Bundle();
			args.putString(CloudFragment.BUNDLE_CLOUD_ID, id);
			replaceFrag.setArguments(args);
			currentCloud = replaceFrag;
			ft.replace(R.id.content_frame, currentCloud);
		}
	}
}
