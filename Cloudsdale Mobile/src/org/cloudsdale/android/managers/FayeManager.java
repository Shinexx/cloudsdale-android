package org.cloudsdale.android.managers;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.faye.CloudsdaleFayeBinder;
import org.cloudsdale.android.faye.CloudsdaleFayeClient;
import org.cloudsdale.android.faye.CloudsdaleFayeListener;
import org.cloudsdale.android.faye.CloudsdaleFayeService;
import org.cloudsdale.android.faye.FayeMessageHandler;
import org.cloudsdale.android.models.CloudsdaleFayeMessage;
import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.api.User;
import org.cloudsdale.android.models.exceptions.QueryException;

import java.util.ArrayList;

public class FayeManager extends ContextWrapper implements ServiceConnection,
		CloudsdaleFayeListener {

	private static CloudsdaleFayeBinder				sFayeBinder;
	private static boolean							sFayeConnected;
	private static boolean							sFirstConnection;
	private static ArrayList<FayeMessageHandler>	sHandlerList;

	public FayeManager(Context base) {
		super(base);
		sHandlerList = new ArrayList<FayeMessageHandler>();
	}

	public static boolean isFayeConnected() {
		return sFayeConnected;
	}

	public void bindFaye() {
		if (sFayeBinder == null) {
			Intent intent = new Intent();
			intent.setClass(Cloudsdale.getContext(),
					CloudsdaleFayeService.class);
			bindService(intent, this, Context.BIND_ABOVE_CLIENT);
		} else {
			if (!sFayeBinder.getFayeClient().isFayeConnected()) {
				sFayeBinder.getFayeClient().connect();
			}
		}
	}

	public void unbindFaye() {
		if (sFayeBinder != null) {
			sFayeBinder.getFayeService().stopFaye();
		}
	}

	private void subscribeToClouds() {
		new Thread() {
			public void run() {
				User me;
				try {
					me = Cloudsdale.getUserManager().getLoggedInUser();
					ArrayList<Cloud> clouds = new ArrayList<Cloud>(
							me.getClouds());
					for (Cloud c : clouds) {
						sFayeBinder.getFayeClient().subscribe(
								"/clouds/" + c.getId() + "/chat/messages");
					}
				} catch (QueryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}

	public void subscribeToMessages(FayeMessageHandler handler) {
		synchronized (sHandlerList) {
			if (!sHandlerList.contains(handler)) {
				sHandlerList.add(handler);
			}
		}
	}

	public void unsubscribeToMessages(FayeMessageHandler handler) {
		synchronized (sHandlerList) {
			sHandlerList.remove(handler);
		}
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		sFayeBinder = (CloudsdaleFayeBinder) service;
		sFayeBinder.getFayeClient().setFayeListener(this);
		sFayeBinder.getFayeService().startFaye();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		sFayeConnected = false;
		sFirstConnection = true;
		sFayeBinder = null;
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
			CloudsdaleFayeMessage message) {
		if (!sHandlerList.isEmpty()) {
			for (FayeMessageHandler handler : sHandlerList) {
				handler.handleMessage(message);
			}
		}
	}

}