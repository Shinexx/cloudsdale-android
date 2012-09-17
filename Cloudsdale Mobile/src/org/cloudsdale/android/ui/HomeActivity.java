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
import org.cloudsdale.android.PersistentData;
import org.cloudsdale.android.R;
import org.cloudsdale.android.exceptions.CloudsdaleQueryException;
import org.cloudsdale.android.faye.FayeMessageHandler;
import org.cloudsdale.android.models.CloudsdaleFayeMessage;
import org.cloudsdale.android.models.LoggedUser;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.Role;
import org.cloudsdale.android.models.api_models.Cloud;
import org.cloudsdale.android.models.api_models.User;
import org.cloudsdale.android.models.queries.CloudGetQuery;
import org.cloudsdale.android.models.queries.UserGetQuery;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class HomeActivity extends SlidingActivity implements FayeMessageHandler {

    private static final String   TAG = "Home Activity";

    private ImageView             mAvatarView;
    private TextView              mUsernameView;
    private TextView              mAccountLevelView;
    private TextView              mDateRegisteredView;
    private TextView              mCloudCountView;
    private SlidingMenu           mSlidingMenu;
    private static ProgressDialog sProgressDialog;
    private static boolean        sShowDialog;

    @SuppressWarnings("rawtypes")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sShowDialog = true;

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

    private void showProgressDialog() {
        sProgressDialog = new ProgressDialog(this);
        sProgressDialog.setIndeterminate(true);
        sProgressDialog.setTitle("Cloudsdale is connecting");
        sProgressDialog.setMessage("Please wait...");
        sProgressDialog.setCancelable(false);
        sProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (sProgressDialog != null && sProgressDialog.isShowing()) {
            sProgressDialog.cancel();
            sShowDialog = false;
        }
        sProgressDialog = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        setViewContent();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateMe();
        getViews();
        setViewContent();
        
        if(!Cloudsdale.isFayeConnected()) {
            Cloudsdale.bindFaye();
            showProgressDialog();
            new Thread() {
                public void run() {
                    while (!Cloudsdale.isFayeConnected()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    hideProgressDialog();
                };
            }.start();
        }
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        sProgressDialog.cancel();
        super.onConfigurationChanged(newConfig);
    }

    private void updateMe() {
        new UserUpdateTask().execute();
        new CloudUpdateTask().execute();
    }

    private void handleCloudsdaleError(CloudsdaleQueryException exception) {
        if (Cloudsdale.DEBUG) {
            Log.d(TAG, exception.getErrorCode() + ":" + exception.getMessage());
        }
    }

    private class UserUpdateTask extends AsyncTask<Void, Void, User> {

        @Override
        protected User doInBackground(Void... params) {
            QueryData data = new QueryData();
            UserGetQuery query = new UserGetQuery(
                    getString(R.string.cloudsdale_api_base)
                            + getString(R.string.cloudsdale_user_endpoint,
                                    PersistentData.getMe().getId()));
            query.addHeader("X-AUTH-TOKEN", PersistentData.getMe()
                    .getAuthToken());
            try {
                return query.execute(data, HomeActivity.this);
            } catch (CloudsdaleQueryException e) {
                HomeActivity.this.handleCloudsdaleError(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(User result) {
            LoggedUser me = PersistentData.getMe();
            adjustUserProperties(me, result);
            PersistentData.storeLoggedUser(me);
            setViewContent();
            super.onPostExecute(result);
        }

        private void adjustUserProperties(LoggedUser me, User result) {
            me.setName(result.getName());
            me.setTimeZone(result.getTimeZone());
            me.setMemberSince(result.getMemberSince());
            me.setSuspendedUntil(me.getSuspendedUntil());
            me.setReasonForSuspension(result.getReasonForSuspension());
            me.setAvatar(result.getAvatar());
            me.setRegistered(result.isRegistered());
            me.setTransientStatus(result.isTransientStatus());
            me.setBanStatus(result.isBanStatus());
            me.setMemberOfACloud(result.isMemberOfACloud());
            me.setHasAvatar(result.isHasAvatar());
            me.setHasReadTnC(result.isHasReadTnC());
            me.setUserRole(result.getRole());
            me.setProsecutions(result.getProsecutions());
        }

    }

    private class CloudUpdateTask extends AsyncTask<Void, Void, Cloud[]> {

        @Override
        protected Cloud[] doInBackground(Void... params) {
            QueryData data = new QueryData();
            LoggedUser me = PersistentData.getMe();
            CloudGetQuery query = new CloudGetQuery(
                    getString(R.string.cloudsdale_api_base)
                            + getString(
                                    R.string.cloudsdale_user_clouds_endpoint,
                                    me.getId()));
            query.addHeader("X-AUTH-TOKEN", me.getAuthToken());
            try {
                return query.executeForCollection(data, HomeActivity.this);
            } catch (CloudsdaleQueryException e) {
                HomeActivity.this.handleCloudsdaleError(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Cloud[] result) {
            LoggedUser me = PersistentData.getMe();
            ArrayList<Cloud> clouds = new ArrayList<Cloud>(
                    Arrays.asList(result));
            me.setClouds(clouds);
            PersistentData.storeLoggedUser(me);
            super.onPostExecute(result);
        }

    }
}
