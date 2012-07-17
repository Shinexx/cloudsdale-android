package org.cloudsdale.android.models;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class QueryData extends Model {

	private String						url;
	private List<BasicNameValuePair>	headers;
	private String						json;

	public List<BasicNameValuePair> getHeaders() {
		return this.headers;
	}

	public String getJson() {
		return this.json;
	}

	public String getUrl() {
		return this.url;
	}
	
	public void setHeaders(ArrayList<BasicNameValuePair> headers) {
		this.headers = headers;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}