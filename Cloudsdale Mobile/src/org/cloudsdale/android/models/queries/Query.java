package org.cloudsdale.android.models.queries;

import android.content.Context;

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

	public final void addListener(ThreadCompleteListener listener) {
		this.thread.addListener(listener);
	}

	public final Thread.State getThreadState() {
		return this.thread.getState();
	}

	public final boolean isAlive() {
		return this.isAlive;
	}

	@Override
	public final void notifyOfThreadComplete(Thread thread) {
		this.isAlive = false;
	}

	protected void setupHttpObjects(String url) {
		this.httpClient = new DefaultHttpClient();
	}

	public final String stripHtml(String htmlString) {
		return android.text.Html.fromHtml(htmlString).toString();
	}

	public abstract Model execute(QueryData data, Context context);

}
