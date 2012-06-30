package org.cloudsdale.android.models.queries;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.params.HttpParams;
import org.cloudsdale.android.models.Model;
import org.cloudsdale.android.models.QueryData;

public abstract class Query {

	protected HttpParams	httpParams;
	protected HttpClient	httpClient;
	protected HttpResponse	httpResponse;
	protected Thread		thread;

	abstract Model execute(QueryData data);

	abstract void setupHttpObjects(String url);
	
	public final Thread.State getThreadState() {
		return thread.getState();
	}
	
	public final boolean isAlive() {
		return thread.isAlive();
	}

}
