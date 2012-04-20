package org.cloudsdale.logic;

public class CloudsdaleAsyncQueryParams {
	private CloudsdaleAsyncQueryWrapper wrapper;
	private String[] params;
	
	public CloudsdaleAsyncQueryParams(CloudsdaleAsyncQueryWrapper wrapper, String[] params) {
		this.wrapper = wrapper;
		this.params = params;
	}
	
	public CloudsdaleAsyncQueryWrapper getWrapper() {
		return wrapper;
	}
	
	public String[] getParams() {
		return params;
	}
	
}
