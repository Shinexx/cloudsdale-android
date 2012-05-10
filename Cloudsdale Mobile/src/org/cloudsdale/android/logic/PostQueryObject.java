package org.cloudsdale.android.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

/**
 * Entity to simply POST queries to Cloudsdale
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class PostQueryObject {
	public static final String	TAG	= "Clousdale PostQueryObject";

	protected HttpParams		httpParams;
	protected HttpPost			httpPost;
	protected HttpClient		httpClient;
	protected HttpResponse		httpResponse;

	/**
	 * Constructor
	 * 
	 * @param entitiyValues
	 *            List of NameValuePairs to be set as the post entity
	 * @param postUrl
	 *            The url that's being posted to
	 */
	public PostQueryObject(List<NameValuePair> entitiyValues, String postUrl) {
		try {
			// Create parameters for connection including 3sec timeout
			// on connection and 5sec timeout on socket
			HttpParams httpParams = new BasicHttpParams();
			int timeoutConnection = 3000;
			int timeoutSocket = 5000;

			// Set the timeouts
			HttpConnectionParams.setConnectionTimeout(httpParams,
					timeoutConnection);
			HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
			httpClient = new DefaultHttpClient(httpParams);

			// Create the data entities
			HttpPost post = new HttpPost(postUrl);

			// Set the POST data
			post.setEntity(new UrlEncodedFormEntity(entitiyValues));
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	/**
	 * Execute the post response
	 * 
	 * @return String with the JSON results
	 */
	public String execute() {
		try {
			httpResponse = httpClient.execute(httpPost);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpResponse.getEntity().getContent()));
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			return builder.toString();
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}

		return null;
	}
}
