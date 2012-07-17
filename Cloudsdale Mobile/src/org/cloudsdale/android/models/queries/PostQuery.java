package org.cloudsdale.android.models.queries;

import org.apache.http.client.methods.HttpPost;

public abstract class PostQuery extends Query {

	protected HttpPost	httpPost;

	@Override
	protected void setupHttpObjects(String url) {
		super.setupHttpObjects(url);
		this.httpPost = new HttpPost(url);
	}
}
