package org.cloudsdale.android.models;


public class QueryData {

	private String						url;
	private String						json;

	public String getJson() {
		return this.json;
	}

	public String getUrl() {
		return this.url;
	}
	
	public void setJson(String json) {
		this.json = json;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}