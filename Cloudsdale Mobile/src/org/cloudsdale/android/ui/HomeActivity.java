package org.cloudsdale.android.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.faye.FayeMessageHandler;
import org.cloudsdale.android.managers.FayeManager;
import org.cloudsdale.android.managers.UserManager;
import org.cloudsdale.android.models.CloudsdaleFayeMessage;
import org.cloudsdale.android.models.Role;
import org.cloudsdale.android.models.api.User;
import org.cloudsdale.android.ui.fragments.SlidingMenuFragment;
import org.holoeverywhere.widget.FrameLayout;
import org.holoeverywhere.widget.TextView;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Activity loaded on the "home" view. Copyright (c) 2012 Cloudsdale.org
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class HomeActivity extends ActivityBase {

	private static final String	TAG	= "Home Activity";

	private View				mLoadingView;
	private View				mContentView;
	private ImageView			mAvatarView;
	private TextView			mUsernameView;
	private TextView			mAccountLevelView;
	private TextView			mDateRegisteredView;
	private TextView			mCloudCountView;
	private TextView			mChatConnectionStatusView;

	public boolean isBehindViewShowing() {
		return mSlidingMenu.isBehindShowing();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set the layouts
		setContentView(R.layout.activity_home);
		setBehindContentView(R.layout.fragment_sliding_menu_host);

		// Get the view objects
		getViews();

		// Bind the Faye service
		if (!FayeManager.isFayeConnected()) {
			FayeManager.bindFaye();
		}

		// Start the UI fill tasks
		new UserViewFillTask().execute();
		new ConnectionMonitorTask().execute();
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
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPause() {
		showAbove();
		super.onPause();
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
		mChatConnectionStatusView = (TextView) findViewById(R.id.home_tile)
				.findViewById(R.id.home_connection_status);
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
			default:
			case NORMAL:
				text = "Welcome back!";
				break;
			case DONOR:
				text = "Thanks for donating!";
				break;
			case DEVELOPER:
				text = "You have code to write! Get to it!";
				break;
			case ADMIN:
				text = "You have things to administrate! NO BREAK FOR YOU!";
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
	 * An async task to grab the user from the manager and then to populate the
	 * home tile based on their information. Copyright (c) 2012 Cloudsdale.org
	 * 
	 * @author Jamison Greeley (atomicrat2552@gmail.com)
	 * 
	 */
	class UserViewFillTask extends AsyncTask<Void, Void, User> {

		@Override
		protected User doInBackground(Void... params) {
			if (Cloudsdale.isDebuggable()) {
				Log.d(TAG, "Executing the UserViewFillTask");
			}
			return UserManager.getLoggedInUser();
		}

		@Override
		protected void onPostExecute(User result) {
			if (Cloudsdale.isDebuggable()) {
				Log.d(TAG, "UserViewFillTask is now filling in the home view");
			}

			// Set the user's avatar in the view
			UrlImageViewHelper.setUrlDrawable(mAvatarView, result.getAvatar()
					.getNormal(), R.drawable.ic_unknown_user);

			// Set the user's username in the view
			mUsernameView.setText(result.getName());

			// Set the user's other properties in the main view
			mAccountLevelView.setText(createAccountLevelText(result
					.getUserRole()));

			// Format and set the user's join date
			Date date = result.getMemberSince();
			SimpleDateFormat df = new SimpleDateFormat("dd MMMM, yyyy");
			mDateRegisteredView.setText(MessageFormat.format(
					"You registered on {0}", df.format(date)));

			// Set the user's cloud count
			mCloudCountView.setText("You are a member of "
					+ String.valueOf(result.getClouds().size()) + " clouds");

			showProgress(false);

			super.onPostExecute(result);
		}
	}

	class ConnectionMonitorTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			while (!FayeManager.isFayeConnected()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mChatConnectionStatusView.setVisibility(View.GONE);
			super.onPostExecute(result);
		}

	}
}
