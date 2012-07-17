package org.cloudsdale.android.models.queries;

import org.apache.http.client.methods.HttpGet;

public abstract class GetQuery extends Query {

	protected HttpGet	httpGet;

	@Override
	protected final void setupHttpObjects(String url) {
		super.setupHttpObjects(url);
		this.httpGet = new HttpGet(url);
	}
}
