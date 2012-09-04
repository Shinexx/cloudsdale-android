package org.cloudsdale.android.models.queries;

import org.apache.http.client.methods.HttpPost;

public abstract class PostQuery extends Query {

	public PostQuery(String url) {
        super(url);
    }

    protected HttpPost	mHttpPost;

	@Override
	protected void setupHttpObjects(String url) {
		super.setupHttpObjects(url);
		this.mHttpPost = new HttpPost(url);
		mHttpPost.setHeader("Accept", "application/json");
	}
	
	@Override
	public void addHeader(String key, String value) {
	    mHttpPost.setHeader(key,value);   
	}
	
	/**
	 * Tell the request that it's content payload is a json string
	 */
	public void setJsonContentType() {
        mHttpPost.setHeader("Content-type", "application/json");
	}
}
