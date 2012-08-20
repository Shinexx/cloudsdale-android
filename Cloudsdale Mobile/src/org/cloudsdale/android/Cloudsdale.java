package org.cloudsdale.android;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.IBinder;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.b3rwynmobile.fayeclient.FayeBinder;
import com.b3rwynmobile.fayeclient.FayeClient;
import com.b3rwynmobile.fayeclient.FayeListener;
import com.b3rwynmobile.fayeclient.FayeService;
import com.b3rwynmobile.fayeclient.models.FayeMessage;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.slidingmenu.lib.SlidingMenu;

import org.cloudsdale.android.faye.FayeMessageHandler;
import org.cloudsdale.android.models.api_models.Cloud;
import org.cloudsdale.android.models.api_models.User;
import org.cloudsdale.android.ui.CloudActivity;
import org.cloudsdale.android.ui.HomeActivity;
import org.cloudsdale.android.ui.adapters.CloudsAdapter;

import java.util.ArrayList;

/**
 * Global application class
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class Cloudsdale extends Application implements ServiceConnection,
        FayeListener {

    // Debug fields
    public static final boolean                  DEBUG            = true;
    private static final String                  TAG              = "Cloudsdale Mobile";

    // Static objects
    private static Cloudsdale                    sAppObject;
    private static FayeBinder                    sFayeBinder;
    private static ArrayList<FayeMessageHandler> sMessageHandlerList;
    private static boolean                       mFirstConnection = true;

    /**
     * Dummy constructor to handle creating static classes and fetch the global
     * app context
     */
    public Cloudsdale() {
        super();
        sMessageHandlerList = new ArrayList<FayeMessageHandler>();
        sAppObject = this;
    }

    public static int dpToPx(int dp, Context ctx) {
        Resources r = ctx.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                r.getDisplayMetrics());
    }

    // originally:
    // http://stackoverflow.com/questions/5418510/disable-the-touch-events-for-all-the-views
    // modified for the needs here
    public static void enableDisableViewGroup(ViewGroup viewGroup,
            boolean enabled) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            if (view.isFocusable()) {
                view.setEnabled(enabled);
            }
            if (view instanceof ViewGroup) {
                Cloudsdale.enableDisableViewGroup((ViewGroup) view, enabled);
            } else if (view instanceof ListView) {
                if (view.isFocusable()) {
                    view.setEnabled(enabled);
                }
                ListView listView = (ListView) view;
                int listChildCount = listView.getChildCount();
                for (int j = 0; j < listChildCount; j++) {
                    if (view.isFocusable()) {
                        listView.getChildAt(j).setEnabled(false);
                    }
                }
            }
        }
    }

    public static void prepareSlideMenu(SlidingMenu slidingMenu,
            Activity context) {
        // View settings
        slidingMenu.showAbove();
        slidingMenu.setBehindOffsetRes(R.dimen.actionbar_home_width);

        // Get all the layout items
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View head = inflater.inflate(R.layout.menu_header, null);
        ListView itemOptions = (ListView) slidingMenu
                .findViewById(android.R.id.list);

        User me = PersistentData.getMe();
        setSlideMenuHeader(head, itemOptions, me);
        addStaticSlideMenuViews(itemOptions, inflater);
        itemOptions.setAdapter(new CloudsAdapter(context, me.getClouds()));
    }

    private static void setSlideMenuHeader(View head, ListView itemOptions,
            User me) {
        itemOptions.addHeaderView(head, null, false);
        itemOptions.setHeaderDividersEnabled(true);

        ImageView userIcon = (ImageView) head
                .findViewById(R.id.slide_menu_user_icon);
        TextView userName = (TextView) head
                .findViewById(R.id.slide_menu_username_label);

        UrlImageViewHelper.setUrlDrawable(userIcon, me.getAvatar().getNormal(),
                R.drawable.unknown_user, 30 * 60 * 1000);
        userName.setText(me.getName());
        head.setClickable(false);
    }

    private static void addStaticSlideMenuViews(ListView list,
            LayoutInflater inflater) {
        View homeView = inflater.inflate(R.layout.cloud_list_item, null);
        View settingsView = inflater.inflate(R.layout.cloud_list_item, null);
        View logoutView = inflater.inflate(R.layout.cloud_list_item, null);

        ImageView homeIcon = (ImageView) homeView.findViewById(R.id.cloud_icon);
        homeIcon.setImageResource(R.drawable.color_icon);
        TextView homeText = (TextView) homeView.findViewById(R.id.cloud_name);
        homeText.setText("Home");
        TextView homeId = (TextView) homeView
                .findViewById(R.id.cloud_hidden_id);
        homeId.setText("Home");
        list.addHeaderView(homeView);

        ImageView settingsIcon = (ImageView) settingsView
                .findViewById(R.id.cloud_icon);
        settingsIcon.setImageResource(R.drawable.color_icon);
        TextView settingsText = (TextView) settingsView
                .findViewById(R.id.cloud_name);
        settingsText.setText("Settings");
        TextView settingsId = (TextView) settingsView
                .findViewById(R.id.cloud_hidden_id);
        settingsId.setText("Settings");
        list.addHeaderView(settingsView);

        ImageView logoutIcon = (ImageView) logoutView
                .findViewById(R.id.cloud_icon);
        logoutIcon.setImageResource(R.drawable.color_icon);
        TextView logoutText = (TextView) logoutView
                .findViewById(R.id.cloud_name);
        logoutText.setText("Log out");
        TextView logoutId = (TextView) logoutView
                .findViewById(R.id.cloud_hidden_id);
        logoutId.setText("Logout");
        list.addHeaderView(logoutView);
    }

    public static void bindFaye() {
        Intent intent = new Intent();
        intent.setClass(sAppObject, FayeService.class);
        sAppObject.bindService(intent, sAppObject, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName className, IBinder binder) {
        sFayeBinder = (FayeBinder) binder;
        sFayeBinder.getFayeClient().setFayeListener(this);
        sFayeBinder.getFayeService().startFaye();
    }

    @Override
    public void onServiceDisconnected(ComponentName className) {
        sFayeBinder = null;
    }

    @Override
    public void connectedToServer(FayeClient faye) {
        if (mFirstConnection) {
            subscribeToClouds();
            mFirstConnection = false;
        }
    }

    @Override
    public void disconnectedFromServer(FayeClient faye) {
        // TODO handle behaviour when disconnected
    }

    @Override
    public void messageReceived(FayeClient faye, FayeMessage msg) {
        if (!sMessageHandlerList.isEmpty()) {
            for (FayeMessageHandler handler : sMessageHandlerList) {
                handler.handleMessage(msg);
            }
        }
    }

    private static void subscribeToClouds() {
        final User me = PersistentData.getMe();
        if (Cloudsdale.DEBUG) {
            Log.d(TAG, "Starting cloud subscriptions");
        }
        for (Cloud c : me.getClouds()) {
            sFayeBinder.getFayeClient().subscribe(
                    "/clouds/" + c.getId() + "/chat/messages");
        }
    }

    public static void subscribeToMessages(FayeMessageHandler handler) {
        sMessageHandlerList.add(handler);
    }

    public static void navigate(String viewId, Activity context) {
        Intent intent = new Intent();
        if (viewId.equals("Home")) {
            intent.setClass(context, HomeActivity.class);
        } else if (viewId.equals("Settings")) {
            // TODO Start the settings view
            intent.setClass(context, HomeActivity.class);
        } else if (viewId.equals("Logout")) {
            // TODO Logout the user
            // TODO Start the login view
            intent.setClass(context, HomeActivity.class);
        } else {
            intent.setClass(context, CloudActivity.class);
            intent.putExtra("cloudId", viewId);
        }
        context.startActivity(intent);
    }
}
