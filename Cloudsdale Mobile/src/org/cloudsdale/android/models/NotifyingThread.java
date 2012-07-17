package org.cloudsdale.android.models;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class NotifyingThread extends Thread {

	protected final Set<ThreadCompleteListener>	listeners	= new CopyOnWriteArraySet<ThreadCompleteListener>();

	public NotifyingThread() {
		super();
	}

	public NotifyingThread(Runnable runnable) {
		super(runnable);
	}

	public final void addListener(final ThreadCompleteListener listener) {
		this.listeners.add(listener);
	}

	protected final void notifyListeners() {
		for (ThreadCompleteListener listener : this.listeners) {
			listener.notifyOfThreadComplete(this);
		}
	}

	public final void removeListener(final ThreadCompleteListener listener) {
		this.listeners.remove(listener);
	}

}
