package org.cloudsdale.android.models;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class NotifyingThread extends Thread {

	protected final Set<ThreadCompleteListener>	listeners	= new CopyOnWriteArraySet<ThreadCompleteListener>();

	public NotifyingThread(Runnable runnable) {
		super(runnable);
	}

	public NotifyingThread() {
		super();
	}

	public final void addListener(final ThreadCompleteListener listener) {
		listeners.add(listener);
	}

	public final void removeListener(final ThreadCompleteListener listener) {
		listeners.remove(listener);
	}

	protected final void notifyListeners() {
		for (ThreadCompleteListener listener : listeners) {
			listener.notifyOfThreadComplete(this);
		}
	}

}
