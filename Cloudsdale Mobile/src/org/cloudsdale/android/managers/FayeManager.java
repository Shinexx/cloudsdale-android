package org.cloudsdale.android.managers;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import org.cloudsdale.android.faye.CloudsdaleFayeBinder;
import org.cloudsdale.android.faye.CloudsdaleFayeClient;
import org.cloudsdale.android.faye.CloudsdaleFayeListener;
import org.cloudsdale.android.faye.CloudsdaleFayeMessage;
import org.cloudsdale.android.faye.CloudsdaleFayeService;
import org.cloudsdale.android.faye.FayeMessageHandler;

import java.util.ArrayList;

public class FayeManager extends ContextWrapper implements ServiceConnection,
		CloudsdaleFayeListener {

	private Context							mBaseContext;
	private CloudsdaleFayeBinder			mFayeBinder;
	private boolean							mFayeConnected;
	private boolean							mFirstConnection;
	private ArrayList<FayeMessageHandler>	mHandlerList;

	public FayeManager(Context base) {
		super(base);
		mHandlerList = new ArrayList<FayeMessageHandler>();
	}

	public boolean isFayeConnected() {
		return mFayeConnected;
	}

	public void bindFaye() {
		if (mFayeBinder == null) {
			Intent intent = new Intent();
			intent.setClass(mBaseContext, CloudsdaleFayeService.class);
			bindService(intent, this, Context.BIND_ABOVE_CLIENT);
		} else {
			if (!mFayeBinder.getFayeClient().isFayeConnected()) {
				mFayeBinder.getFayeClient().connect();
			}
		}
	}

	public void unbindFaye() {
		if (mFayeBinder != null) {
			mFayeBinder.getFayeService().stopFaye();
		}
	}

	private void subscribeToClouds() {
		// TODO subscribe to all user clouds
	}

	public void subscribeToMessages(FayeMessageHandler handler) {
		synchronized (mHandlerList) {
			if (!mHandlerList.contains(handler)) {
				mHandlerList.add(handler);
			}
		}
	}

	public void unsubscribeToMessages(FayeMessageHandler handler) {
		synchronized (mHandlerList) {
			mHandlerList.remove(handler);
		}
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		mFayeBinder = (CloudsdaleFayeBinder) service;
		mFayeBinder.getFayeClient().setFayeListener(this);
		mFayeBinder.getFayeService().startFaye();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		mFayeConnected = false;
		mFirstConnection = true;
		mFayeBinder = null;
	}

	@Override
	public void connectedToServer(CloudsdaleFayeClient faye) {
		mFayeConnected = true;
		if (mFirstConnection) {
			subscribeToClouds();
			mFirstConnection = false;
		}
	}

	@Override
	public void disconnectedFromServer(CloudsdaleFayeClient faye) {
		mFayeConnected = false;
	}

	@Override
	public void messageReceived(CloudsdaleFayeClient faye,
			CloudsdaleFayeMessage message) {
		if (!mHandlerList.isEmpty()) {
			for (FayeMessageHandler handler : mHandlerList) {
				handler.handleMessage(message);
			}
		}
	}

}