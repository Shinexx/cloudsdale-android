package org.cloudsdale.android.models.queries;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;

public abstract class GetQuery extends Query {

	protected HttpGet	httpGet;

	@Override
	protected final void setupHttpObjects(String url) {
		super.setupHttpObjects(url);
		this.httpGet = new HttpGet(url);
	}

	protected void setHeaders(List<BasicNameValuePair> headers) {
		if (headers != null) {
			for (BasicNameValuePair nvp : headers) {
				if (nvp.getName().toLowerCase().equals("x-auth-token")) {
					httpGet.setHeader(nvp.getName(),
							nvp.getValue());
				}
			}
		}
	}
}
