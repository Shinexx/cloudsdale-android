package org.cloudsdale.android.queries;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;

public class Query extends AsyncTask<String, Void, String> {
	
	public static final int URL_INDEX = 0;
	
	protected HttpParams httpParams;
	protected HttpPost httpPost;
	protected HttpClient httpClient;
	protected HttpResponse httpResponse;

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		return null;
	}

}
