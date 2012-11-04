package org.cloudsdale.android.ui;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

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

	private static final String		TAG	= "Home Activity";

	private View					mLoadingView;
	private View					mContentView;
	private ImageView				mAvatarView;
	private TextView				mUsernameView;
	private TextView				mAccountLevelView;
	private TextView				mDateRegisteredView;
	private TextView				mCloudCountView;
	private SlidingMenu				mSlidingMenu;
	private static ProgressDialog	sProgressDialog;

	@SuppressWarnings("rawtypes")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set the layouts
		setContentView(R.layout.activity_home);
		setBehindContentView(R.layout.menu);

		// Get the view objects
		getViews();
		mSlidingMenu = getSlidingMenu();
		Cloudsdale.prepareSlideMenu(mSlidingMenu, this);

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
	protected void onResume() {
		super.onResume();

		setViewContent();

		// if (!Cloudsdale.isFayeConnected()) {
		// Cloudsdale.bindFaye();
		// showProgressDialog();
		// new Thread() {
		// public void run() {
		// while (!Cloudsdale.isFayeConnected()) {
		// try {
		// Thread.sleep(100);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		// hideProgressDialog();
		// };
		// }.start();
		// }
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
	 * Starts an async task to load our view content without blocking the UI
	 */
	private void setViewContent() {
		new UserViewFillTask().execute();
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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		sProgressDialog.cancel();
		super.onConfigurationChanged(newConfig);
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

			// Switch the visible layout
			mLoadingView.setVisibility(View.INVISIBLE);
			mContentView.setVisibility(View.VISIBLE);

			// Set the user's avatar in the view
			UrlImageViewHelper.setUrlDrawable(mAvatarView, result.getAvatar()
					.getNormal(), R.drawable.unknown_user);

			// Set the user's username in the view
			mUsernameView.setText(result.getName());

			// Set the user's other properties in the main view
			mAccountLevelView.setText(createAccountLevelText(result.getRole()));

			// Format and set the user's join date
			Date date = result.getMemberSince().getTime();
			SimpleDateFormat df = new SimpleDateFormat("dd MM, yyyy");
			mDateRegisteredView.setText(MessageFormat.format(
					"You registered on {0}", df.format(date)));

			// Set the user's cloud count
			mCloudCountView.setText("You are a member of "
					+ String.valueOf(result.getClouds().size()) + " clouds");

			super.onPostExecute(result);
		}
	}
}
