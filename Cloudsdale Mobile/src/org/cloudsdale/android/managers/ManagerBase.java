package org.cloudsdale.android.managers;

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

}
