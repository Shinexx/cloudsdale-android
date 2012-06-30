package org.cloudsdale.android.models.queries;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public abstract class GetQuery extends Query {

	protected HttpGet	httpGet;

	@Override
	protected final void setupHttpObjects(String url) {
		httpGet = new HttpGet(url);
		httpClient = new DefaultHttpClient();
	}
}
