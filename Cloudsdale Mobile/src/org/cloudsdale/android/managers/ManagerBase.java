package org.cloudsdale.android.managers;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class ManagerBase {

	// Thread locks
	protected final ReentrantReadWriteLock	mReadWriteLock	= new ReentrantReadWriteLock(
																	true);
	protected final Lock					mReadLock		= mReadWriteLock
																	.readLock();
	protected final Lock					mWriteLock		= mReadWriteLock
																	.writeLock();

	// Worker thead stuff
	protected HandlerThread					mNetworkThread;
	protected Handler						mNetworkHandler;

	public ManagerBase(String name) {
		mNetworkThread = new HandlerThread(name);
		mNetworkThread.start();
		mNetworkHandler = new Handler(mNetworkThread.getLooper());
	}
}
