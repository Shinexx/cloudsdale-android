package org.cloudsdale.android.queries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;

import com.bugsense.trace.BugSenseHandler;

public class UserCloudQuery extends AsyncTask<String, Void, String> {

	private static final String TAG = "UserCloudQuery";

	protected HttpParams httpParams;
	protected HttpGet httpGet;
	protected HttpClient httpClient;
	protected HttpResponse httpResponse;

	@Override
	protected String doInBackground(String... params) {

		// Create the data entity
		httpGet = new HttpGet(params[0]);

		// Timeout params in millis
		httpParams = new BasicHttpParams();
		int timeoutConnection = 30000;
		int timeoutSocket = 30000;

		// Set the timeouts
		HttpConnectionParams
				.setConnectionTimeout(httpParams, timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
		httpClient = new DefaultHttpClient(httpParams);

		// Get the cloud list
		try {
			// Fetch the clouds
			httpResponse = httpClient.execute(httpGet);

			// Stringify the JSON
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpResponse.getEntity().getContent()));
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			// Send the JSON back to the caller
			return builder.toString();
		} catch (ClientProtocolException e) {
			BugSenseHandler.log(TAG, e);
		} catch (IOException e) {
			BugSenseHandler.log(TAG, e);
		}

		return null;
	}

}
