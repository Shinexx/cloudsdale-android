package org.cloudsdale.android.ui;

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
import org.cloudsdale.android.PersistentData;
import org.cloudsdale.android.R;
import org.cloudsdale.android.faye.FayeMessageHandler;
import org.cloudsdale.android.models.CloudsdaleFayeMessage;
import org.cloudsdale.android.models.Role;
import org.cloudsdale.android.models.api_models.User;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends SlidingActivity implements FayeMessageHandler {

    private static final String TAG = "Home Activity";

    private ImageView           mAvatarView;
    private TextView            mUsernameView;
    private TextView            mAccountLevelView;
    private TextView            mDateRegisteredView;
    private TextView            mCloudCountView;
    private TextView            mWarningCountView;
    private SlidingMenu         mSlidingMenu;

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
        Cloudsdale.bindFaye();
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
                if (mSlidingMenu.isBehindShowing()) {
                    mSlidingMenu.showAbove();
                } else {
                    mSlidingMenu.showBehind();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getViews() {
        if (mAvatarView == null) {
            mAvatarView = (ImageView) findViewById(R.id.home_user_avatar);
        }

        if (mUsernameView == null) {
            mUsernameView = (TextView) findViewById(R.id.home_username_label);
        }

        if (mAccountLevelView == null) {
            mAccountLevelView = (TextView) findViewById(R.id.home_account_level_label);
        }

        if (mDateRegisteredView == null) {
            mDateRegisteredView = (TextView) findViewById(R.id.home_register_date_label);
        }

        if (mCloudCountView == null) {
            mCloudCountView = (TextView) findViewById(R.id.home_cloud_count_label);
        }

        if (mWarningCountView == null) {
            mWarningCountView = (TextView) findViewById(R.id.home_warning_count_label);
        }
    }

    private void setViewContent() {
        User me = PersistentData.getMe();

        // Set the user's avatar in the view
        UrlImageViewHelper.setUrlDrawable(mAvatarView, me.getAvatar()
                .getNormal(), R.drawable.unknown_user);

        // Set the user's username in the view
        mUsernameView.setText(me.getName());

        // Set the user's other properties in the main view
        mAccountLevelView.setText(createAccountLevelText(me.getRole()));

        // Format and set the user's join date
        Date date = me.getMemberSince().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MM, yyyy");
        mDateRegisteredView.setText(MessageFormat.format(
                "You registered on {0}", df.format(date)));

        // Set the user's cloud count
        mCloudCountView.setText("You are a member of "
                + String.valueOf(me.getClouds().size()) + " clouds");

        // Set the user's warning count
        mWarningCountView.setText("You have "
                + String.valueOf(me.getProsecutions().length) + " warnings");

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

}
