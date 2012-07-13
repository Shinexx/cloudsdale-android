package org.cloudsdale.android.models.queries;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.cloudsdale.android.models.Model;
import org.cloudsdale.android.models.NotifyingThread;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.ThreadCompleteListener;

public abstract class Query implements ThreadCompleteListener {

	protected HttpClient		httpClient;
	protected HttpResponse		httpResponse;
	protected NotifyingThread	thread;
	protected boolean			isAlive;

	abstract Model execute(QueryData data);

	protected void setupHttpObjects(String url) {
		httpClient = new DefaultHttpClient();
	}

	public final Thread.State getThreadState() {
		return thread.getState();
	}

	public final boolean isAlive() {
		return isAlive;
	}

	public final String stripHtml(String htmlString) {
		return android.text.Html.fromHtml(htmlString).toString();
	}

	public final void addListener(ThreadCompleteListener listener) {
		thread.addListener(listener);
	}
	
	@Override
	public final void notifyOfThreadComplete(Thread thread) {
		isAlive = false;
	}
	
}
