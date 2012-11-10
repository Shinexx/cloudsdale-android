package org.cloudsdale.android;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.IBinder;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.cloudsdale.android.faye.CloudsdaleFayeBinder;
import org.cloudsdale.android.faye.CloudsdaleFayeClient;
import org.cloudsdale.android.faye.CloudsdaleFayeListener;
import org.cloudsdale.android.faye.CloudsdaleFayeService;
import org.cloudsdale.android.faye.FayeMessageHandler;
import org.cloudsdale.android.managers.UserManager;
import org.cloudsdale.android.models.CloudsdaleFayeMessage;
import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.api.User;

import java.util.ArrayList;

/**
 * Global application class
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class Cloudsdale extends Application implements ServiceConnection,
		CloudsdaleFayeListener {

	// Debug fields
	public static final boolean						DEBUG				= true;
	private static final String						TAG					= "Cloudsdale Mobile";

	// Thirty minutes
	public static final int							AVATAR_EXPIRATION	= 30 * 60 * 1000;
	public static final int							CLOUD_EXPIRATION	= 1000 * 60 * 60;

	// Static objects
	private static Cloudsdale						sAppObject;
	private static CloudsdaleFayeBinder				sFayeBinder;
	private static ArrayList<FayeMessageHandler>	sMessageHandlerList;
	private static boolean							sFirstConnection	= true;
	private static boolean							sFayeConnected;
	private static Gson								sJsonDeserializer;

	// Public data objects
	private static String							sCloudShowing;

	/**
	 * Dummy constructor to handle creating static classes and fetch the global
	 * app context
	 */
	public Cloudsdale() {
		super();
		sMessageHandlerList = new ArrayList<FayeMessageHandler>();
		sAppObject = this;
		sFayeConnected = false;
	}

	public static boolean isFayeConnected() {
		return sFayeConnected;
	}

	public static void setShowingCloud(String cloudId) {
		sCloudShowing = cloudId;
	}

	public static String getShowingCloud() {
		return sCloudShowing;
	}

	public static Context getContext() {
		return sAppObject;
	}

	public static Gson getJsonDeserializer() {
		if (sJsonDeserializer == null) {
			GsonBuilder builder = new GsonBuilder();
			sJsonDeserializer = builder.create();
		}
		return sJsonDeserializer;
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

	public static void bindFaye() {
		if (sFayeBinder == null) {
			Intent intent = new Intent();
			intent.setClass(sAppObject.getApplicationContext(),
					CloudsdaleFayeService.class);
			sAppObject
					.bindService(intent, sAppObject, Context.BIND_AUTO_CREATE);
		} else {
			if (!sFayeBinder.getFayeClient().isFayeConnected()) {
				sFayeBinder.getFayeClient().connect();
			}
		}
	}

	public static void unbindFaye() {
		if (sFayeBinder != null) {
			sFayeBinder.getFayeService().stopFaye();
		}
	}

	@Override
	public void onServiceConnected(ComponentName className, IBinder binder) {
		if (DEBUG) {
			Toast.makeText(sAppObject, "Cloudsale Faye service bound",
					Toast.LENGTH_LONG).show();
			Log.d(TAG, "Faye service bound");
		}
		sFayeBinder = (CloudsdaleFayeBinder) binder;
		sFayeBinder.getFayeClient().setFayeListener(this);
		sFayeBinder.getFayeService().startFaye();
	}

	@Override
	public void onServiceDisconnected(ComponentName className) {
		sFayeConnected = false;
		sFirstConnection = true;
		sFayeBinder = null;
	}

	private static void subscribeToClouds() {
		if (Cloudsdale.DEBUG) {
			Log.d(TAG, "Starting cloud subscriptions");
		}
		new Thread() {
			public void run() {
				User me = null;
				while (me == null) {
					try {
						Thread.sleep(100);
						me = UserManager.getLoggedInUser();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				for (Cloud c : me.getClouds()) {
					sFayeBinder.getFayeClient().subscribe(
							"/clouds/" + c.getId() + "/chat/messages");
				}
			};
		}.start();
	}

	public static void subscribeToMessages(FayeMessageHandler handler) {
		synchronized (sMessageHandlerList) {
			if (!sMessageHandlerList.contains(handler)) {
				sMessageHandlerList.add(handler);
			}
		}
	}

	public static void unsubscribeToMessages(FayeMessageHandler handler) {
		synchronized (sMessageHandlerList) {
			sMessageHandlerList.remove(handler);
		}
	}

	@Override
	public void connectedToServer(CloudsdaleFayeClient faye) {
		sFayeConnected = true;
		if (sFirstConnection) {
			subscribeToClouds();
			sFirstConnection = false;
		}
	}

	@Override
	public void disconnectedFromServer(CloudsdaleFayeClient faye) {
		sFayeConnected = false;
	}

	@Override
	public void messageReceived(CloudsdaleFayeClient faye,
			CloudsdaleFayeMessage msg) {
		if (!sMessageHandlerList.isEmpty()) {
			for (FayeMessageHandler handler : sMessageHandlerList) {
				handler.handleMessage(msg);
			}
		}
	}
}
