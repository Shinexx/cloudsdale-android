package org.cloudsdale.android.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.b3rwynmobile.fayeclient.FayeBinder;
import com.b3rwynmobile.fayeclient.FayeClient;
import com.b3rwynmobile.fayeclient.FayeListener;
import com.b3rwynmobile.fayeclient.FayeService;
import com.b3rwynmobile.fayeclient.models.FayeMessage;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;

import org.apache.http.message.BasicNameValuePair;
import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.PersistentData;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.LoggedUser;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.api_models.Cloud;
import org.cloudsdale.android.models.api_models.User;
import org.cloudsdale.android.models.enums.Role;
import org.cloudsdale.android.models.queries.CloudGetQuery;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class HomeActivity extends SlidingActivity implements ServiceConnection,
		FayeListener {

	@SuppressWarnings("unused")
	private static final String	TAG	= "Home Activity";

	private ImageView			avatarView;
	private TextView			usernameView;
	private TextView			accountLevelView;
	private TextView			dateRegisteredView;
	private TextView			cloudCountView;
	private TextView			warningCountView;
	private SlidingMenu			slidingMenu;
	private FayeBinder			fayeBinder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set the layouts
		setContentView(R.layout.activity_home);
		setBehindContentView(R.layout.menu);

		// Get the view objects
		getViews();
		slidingMenu = getSlidingMenu();
		Cloudsdale.prepareSlideMenu(slidingMenu, this);

		// Customize actionbar
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);

		Intent intent = new Intent();
		intent.setClass(getApplication(), FayeService.class);
		bindService(intent, this, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStart() {
		super.onStart();

		// updateClouds();
		setViewContent();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// updateClouds();
		getViews();
		setViewContent();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if (slidingMenu.isBehindShowing()) {
					slidingMenu.showAbove();
				} else {
					slidingMenu.showBehind();
				}
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void updateClouds() {
		final LoggedUser me = PersistentData.getMe(this);
		new Thread(new Runnable() {
			@Override
			public void run() {
				QueryData data = new QueryData();
				String unformattedUrl = getString(R.string.cloudsdale_api_base)
						+ getString(R.string.cloudsdale_user_clouds_endpoint);
				data.setUrl(MessageFormat.format(unformattedUrl, me.getId()));
				ArrayList<BasicNameValuePair> headers = new ArrayList<BasicNameValuePair>();
				headers.add(new BasicNameValuePair("X-AUTH-TOKEN", me
						.getAuthToken()));
				data.setHeaders(headers);
				Cloud[] clouds = new CloudGetQuery().executeForCollection(data,
						HomeActivity.this);
				me.setClouds((ArrayList<Cloud>) Arrays.asList(clouds));
			}
		}).start();
	}

	private void getViews() {
		if (avatarView == null) {
			avatarView = (ImageView) findViewById(R.id.home_user_avatar);
		}

		if (usernameView == null) {
			usernameView = (TextView) findViewById(R.id.home_username_label);
		}

		if (accountLevelView == null) {
			accountLevelView = (TextView) findViewById(R.id.home_account_level_label);
		}

		if (dateRegisteredView == null) {
			dateRegisteredView = (TextView) findViewById(R.id.home_register_date_label);
		}

		if (cloudCountView == null) {
			cloudCountView = (TextView) findViewById(R.id.home_cloud_count_label);
		}

		if (warningCountView == null) {
			warningCountView = (TextView) findViewById(R.id.home_warning_count_label);
		}
	}

	private void setViewContent() {
		User me = PersistentData.getMe(HomeActivity.this);

		// Set the user's avatar in the view
		UrlImageViewHelper.setUrlDrawable(avatarView, me.getAvatar()
				.getNormal(), R.drawable.unknown_user);

		// Set the user's username in the view
		usernameView.setText(me.getName());

		// Set the user's other properties in the main view
		accountLevelView.setText(createAccountLevelText(me.getRole()));

		// Format and set the user's join date
		Date date = me.getMemberSince().getTime();
		SimpleDateFormat df = new SimpleDateFormat("dd MM, yyyy");
		dateRegisteredView.setText(MessageFormat.format(
				"You registered on {0}", df.format(date)));

		// Set the user's cloud count
		cloudCountView.setText("You are a member of "
				+ String.valueOf(me.getClouds().size()) + " clouds");

		// Set the user's warning count
		warningCountView.setText("You have "
				+ String.valueOf(me.getProsecutions().length) + " warnings");

	}

	private void subscribeToClouds() {
		final User me = PersistentData.getMe(this);
		Log.d(TAG, "Starting cloud subscriptions");
		for (Cloud c : me.getClouds()) {
			HomeActivity.this.fayeBinder.getFayeClient().subscribe(
					"/cloud/" + c.getId());
		}
	}

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
			case ADMIN:
				text = "You have code to write! Get to it!";
				break;
			case CREATOR:
				text = "Go back to iOS douchebag!";
				break;
		}

		return text;
	}

	@Override
	public void onServiceConnected(ComponentName className, IBinder binder) {
		fayeBinder = (FayeBinder) binder;
		fayeBinder.getFayeClient().setFayeListener(this);
		fayeBinder.getFayeService().startFaye();
	}

	@Override
	public void onServiceDisconnected(ComponentName className) {
		fayeBinder = null;
	}

	@Override
	public void connectedToServer(FayeClient faye) {
		subscribeToClouds();
	}

	@Override
	public void disconnectedFromServer(FayeClient faye) {

	}

	@Override
	public void messageReceived(FayeClient faye, FayeMessage msg) {
		// TODO parse messages
	}

}
