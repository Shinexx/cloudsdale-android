package org.cloudsdale.android.managers;

import org.cloudsdale.android.Cloudsdale;

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
	
	protected Cloudsdale mAppInstance;

	public ManagerBase(Cloudsdale application) {
		this.mAppInstance = application;
	}
	
}
