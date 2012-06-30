package org.cloudsdale.android.models.queries;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public abstract class PostQuery extends Query {

	protected HttpPost httpPost;
	
	protected final void setupHttpObjects(String url) {
		httpPost = new HttpPost(url);
		httpClient = new DefaultHttpClient();
	}
}
