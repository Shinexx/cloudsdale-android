package org.cloudsdale.android.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.cloudsdale.android.PersistentData;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.api_models.User;
import org.cloudsdale.android.models.enums.Role;

public class HomeActivity extends SherlockFragmentActivity implements SlideMenuInterface.OnSlideMenuItemClickListener {

	private ImageView	avatarView;
	private TextView	usernameView;
	private TextView	accountLevelView;
	private TextView	dateRegisteredView;
	private TextView	cloudCountView;
	private TextView	warningCountView;
	private SlideMenu	slideMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		getViews();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		slideMenu = new SlideMenu(this, R.layout.menu, this, 333);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		setViewContent();
	}

	@Override
	protected void onResume() {
		super.onResume();

		getViews();
		setViewContent();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				this.slideMenu.show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}
	
	@Override
	public void onBackPressed() {
		if (this.slideMenu.isShowing()) {
			this.slideMenu.hide();
		} else {
			super.onBackPressed();
		}
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

		UrlImageViewHelper.setUrlDrawable(avatarView, me.getAvatar()
				.getNormal(), R.drawable.unknown_user);

		usernameView.setText(me.getName());

		accountLevelView.setText(createAccountLevelText(me.getRole()));

		dateRegisteredView.setText("Placeholder");

		cloudCountView.setText("You are a member of " + String.valueOf(me.getClouds().size()) + " clouds");

		warningCountView.setText("You have " + String.valueOf(me.getProsecutions().length) + " warnings");
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
				text = "Now, either someone was expirementing on you, or your broke something";
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
	public void onSlideMenuItemClick(int itemId) {
		// TODO Auto-generated method stub
	}
}
