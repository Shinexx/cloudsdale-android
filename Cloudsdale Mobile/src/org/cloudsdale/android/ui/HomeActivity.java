package org.cloudsdale.android.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.WazaBe.HoloEverywhere.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.faye.FayeMessageHandler;
import org.cloudsdale.android.managers.UserManager;
import org.cloudsdale.android.models.CloudsdaleFayeMessage;
import org.cloudsdale.android.models.Role;
import org.cloudsdale.android.models.api.User;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Activity loaded on the "home" view.
 * Copyright (c) 2012 Cloudsdale.org
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class HomeActivity extends SlidingActivity implements FayeMessageHandler {

	private static final String	TAG	= "Home Activity";

	private View				mLoadingView;
	private View				mContentView;
	private ImageView			mAvatarView;
	private TextView			mUsernameView;
	private TextView			mAccountLevelView;
	private TextView			mDateRegisteredView;
	private TextView			mCloudCountView;
	private SlidingMenu			mSlidingMenu;
	private Activity			mActivityContext;
	private boolean				mAccountLoaded;

	@SuppressWarnings("rawtypes")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivityContext = this;
		mAccountLoaded = false;

		// Set the layouts
		setContentView(R.layout.activity_home);
		setBehindContentView(R.layout.menu);

		// Get the view objects
		getViews();
		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setSlidingEnabled(false);

		// Customize actionbar
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);

		// Bind the Faye service
		Cloudsdale.subscribeToMessages(this);

		// Set the item listener for the menu
		((AdapterView) mSlidingMenu.findViewById(android.R.id.list))
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						showAbove();
						Cloudsdale.navigate(((TextView) view
								.findViewById(R.id.cloud_hidden_id)).getText()
								.toString(), HomeActivity.this);
					}
				});
	}

	@Override
	protected void onStart() {
		super.onStart();

		new UserViewFillTask().execute();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if (mSlidingMenu.isBehindShowing()) {
					mSlidingMenu.showAbove();
				} else {
					if (mAccountLoaded) {
						mSlidingMenu.showBehind();
					} else {
						Toast.makeText(
								this,
								"Please wait for your account to finish loading",
								Toast.LENGTH_LONG).show();
					}
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Fetches and programmatically binds all the different view objects we care
	 * about
	 */
	private void getViews() {
		mLoadingView = findViewById(R.id.loading_frame);
		mContentView = findViewById(R.id.main_content);
		mAvatarView = (ImageView) findViewById(R.id.home_user_avatar);
		mUsernameView = (TextView) findViewById(R.id.home_username_label);
		mAccountLevelView = (TextView) findViewById(R.id.home_account_level_label);
		mDateRegisteredView = (TextView) findViewById(R.id.home_register_date_label);
		mCloudCountView = (TextView) findViewById(R.id.home_cloud_count_label);
	}

	/**
	 * Creates the appropriate text for the home tile based on the user's role
	 * 
	 * @param role
	 *            The user's role
	 * @return The text string that should be presented in the home tile
	 */
	private String createAccountLevelText(Role role) {
		String text = "";

		switch (role) {
			case NORMAL:
				text = "Welcome back!";
				break;
			case DONOR:
				text = "Thanks for donating!";
				break;
			case PLACEHOLDER:
				text = "Now, either someone was expirementing on you, or you broke something";
				break;
			case MODERATOR:
				text = "Get to work moderation monkey!";
				break;
			case DEVELOPER:
			case ADMIN:
				text = "You have code to write! Get to it!";
				break;
			case FOUNDER:
				text = "Go back to iOS douchebag!";
				break;
		}

		return text;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoadingView.setVisibility(View.VISIBLE);
			mLoadingView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoadingView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mContentView.setVisibility(View.VISIBLE);
			mContentView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mContentView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
			mContentView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Handles Cloudsdale messages directed at this activity
	 */
	@Override
	public void handleMessage(CloudsdaleFayeMessage message) {
		String channel = message.getChannel().substring(1);
		String cloudId = channel.split("/")[1];
		if (Cloudsdale.DEBUG) {
			Log.d(TAG, "Handling message for channel " + channel
					+ " which generated a cloud id of " + cloudId);
		}
		// TODO Handle the unread message counts
	}

	/**
	 * An async task to grab the user from the manager and then to populate the
	 * home tile based on their information.
	 * Copyright (c) 2012 Cloudsdale.org
	 * 
	 * @author Jamison Greeley (atomicrat2552@gmail.com)
	 * 
	 */
	class UserViewFillTask extends AsyncTask<Void, Void, User> {

		@Override
		protected User doInBackground(Void... params) {
			if (Cloudsdale.DEBUG) {
				Log.d(TAG, "Executing the UserViewFillTask");
			}
			return UserManager.getLoggedInUser();
		}

		@Override
		protected void onPostExecute(User result) {
			if (Cloudsdale.DEBUG) {
				Log.d(TAG, "UserViewFillTask is now filling in the home view");
			}

			// Set the user's avatar in the view
			UrlImageViewHelper.setUrlDrawable(mAvatarView, result.getAvatar()
					.getNormal(), R.drawable.unknown_user);

			// Set the user's username in the view
			mUsernameView.setText(result.getName());

			// Set the user's other properties in the main view
			mAccountLevelView.setText(createAccountLevelText(result.getRole()));

			// Format and set the user's join date
			Date date = result.getMemberSince().getTime();
			SimpleDateFormat df = new SimpleDateFormat("dd MMMM, yyyy");
			mDateRegisteredView.setText(MessageFormat.format(
					"You registered on {0}", df.format(date)));

			// Set the user's cloud count
			mCloudCountView.setText("You are a member of "
					+ String.valueOf(result.getClouds().size()) + " clouds");

			showProgress(false);
			Cloudsdale.prepareSlideMenu(mSlidingMenu, mActivityContext);
			mSlidingMenu.setSlidingEnabled(true);
			mAccountLoaded = true;

			super.onPostExecute(result);
		}
	}
}
