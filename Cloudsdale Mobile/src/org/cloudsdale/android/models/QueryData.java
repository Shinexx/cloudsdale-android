package org.cloudsdale.android.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class QueryData extends Model {

	private String						url;
	private List<BasicNameValuePair>	headers;
	private String						json;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<BasicNameValuePair> getHeaders() {
		return headers;
	}

	public void setHeaders(ArrayList<BasicNameValuePair> headers) {
		this.headers = headers;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

}
