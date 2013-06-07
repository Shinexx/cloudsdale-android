package org.cloudsdale.android.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.androidquery.AQuery;
import com.koushikdutta.async.future.FutureCallback;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.DataStore;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.api.Session;
import org.cloudsdale.android.models.api.User;
import org.cloudsdale.android.models.network.Provider;
import org.cloudsdale.android.ui.fragments.AboutFragment;
import org.cloudsdale.android.ui.fragments.AboutFragment_;
import org.cloudsdale.android.ui.fragments.CloudFragment;
import org.cloudsdale.android.ui.fragments.CloudFragment_;
import org.cloudsdale.android.ui.fragments.HomeFragment;
import org.cloudsdale.android.ui.fragments.HomeFragment_;
import org.cloudsdale.android.ui.fragments.LoginFragment;
import org.cloudsdale.android.ui.fragments.SlidingMenuFragment;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Base activity to do core setup. <br/>
 * Copyright (c) 2012 Cloudsdale.org
 * 
 * @author Jamison Greeley (berwyn.codeweaver@gmail.com)
 * 
 */
@EActivity(R.layout.activity_base)
public class CloudsdaleActivity extends FragmentActivity implements
		SlidingMenuFragment.ISlidingMenuFragmentCallbacks, ActivityCallbacks {

	// Suppressing Lint warning - Resource object doesn't exist at compile time,
	// asset IDs do thanks to R.java
	@SuppressLint("ResourceAsColor")
	public static final Style							INFINITE			= new Style.Builder()
																					.setBackgroundColor(
																							android.R.color.holo_red_light)
																					.build();

	private static final String							TAG					= "Cloudsdale Activity";
	private static final String							SAVED_FRAGMENT_KEY	= "savedFragment";

	private HomeFragment								homeFragment;
	private AboutFragment								aboutFragment;
	private LoginFragment								loginFragment;
	private SlidingMenuFragment							slidingFragment;
	private boolean										isOnTablet;
	private AQuery										aQuery;
	private Map<String, SoftReference<CloudFragment>>	cloudFragments;
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
		aQuery = new AQuery(this);
		cloudFragments = new HashMap<String, SoftReference<CloudFragment>>();

		isOnTablet = findViewById(R.id.tablet_menu) != null;
		generateFragments();
		if (!isOnTablet) {
			// TODO Hook up the sliding menu
		}

		FragmentManager fm = getSupportFragmentManager();
		slidingFragment = (SlidingMenuFragment) fm
				.findFragmentByTag(slidingFragmentTag);
		slidingFragment.setCallback(this);

		if (savedInstanceState != null
				&& savedInstanceState.containsKey(SAVED_FRAGMENT_KEY)) {
			// TODO replace fragments when we icicle the activity
		} else {
			// TODO Configure Cloudsdale endpoints here
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

	private void refreshSlidingMenuClouds(User user) {
		// TODO Reimplement using the DrawerLayout
	}

	private void handleSessionRenewal() {
		if (DataStore.getActiveAccount() == null) {
			if (cloudsdale.isDebuggable()) {
				Log.d(TAG, "Renewing Session");
			}
			String accountId = DataStore.getAccountIds()[0];
			cloudsdale.callZephyr().postSession(CloudsdaleActivity.this,
					Provider.CLOUDSDALE, accountId, authToken,
					new FutureCallback<Session>() {
						@Override
						public void onCompleted(Exception e, Session session) {
                            // TODO Handle session renewal!
						}
					});
		} else {
			if (cloudsdale.isDebuggable()) {
				Log.d(TAG, "No session renewal required, inflating home view");
			}
			homeFragment.inflateHomeCards(cloudsdale.getLoggedInUser());
		}
	}

	private void replaceCloudFragment(String id, FragmentTransaction ft) {
		CloudFragment replaceFrag;
		if (!cloudFragments.containsKey(id)
				|| cloudFragments.get(id).get() == null) {
			replaceFrag = new CloudFragment_();
			cloudFragments.put(id, new SoftReference<CloudFragment>(
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
