package org.cloudsdale.logic;

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
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.AsyncTask;
import android.util.Log;

public class CloudsdaleAsyncGetQuery extends
		AsyncTask<CloudsdaleAsyncQueryParams, String, JSONObject> {
	
	private CloudsdaleAsyncQueryWrapper wrapper;

	@Override
	protected JSONObject doInBackground(CloudsdaleAsyncQueryParams... params) {
		wrapper = params[0].getWrapper();
		String apiUrl = params[0].getParams()[0];
		String query = params[0].getParams()[1] == null ? "" : params[0]
				.getParams()[1];

		try {
			// Create the params for connection including 3sec timeout on
			// connection and 5sec timeout on socket
			HttpParams httpParams = new BasicHttpParams();
			int timeoutConnection = 3000;
			int timeoutSocket = 5000;

			// Set the timeouts
			HttpConnectionParams.setConnectionTimeout(httpParams,
					timeoutConnection);
			HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
			HttpClient httpClient = new DefaultHttpClient(httpParams);

			// Create the data entities
			HttpGet get = new HttpGet(apiUrl);
			HttpResponse response;

			// Query the server
			response = httpClient.execute(get);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null;) {
				builder.append(line).append("\n");
			}

			JSONTokener tokener = new JSONTokener(builder.toString());
			JSONObject result = new JSONObject(tokener);
			return result;
		} catch (ClientProtocolException e) {
			Log.e("Cloudsdale", e.getMessage());
		} catch (IOException e) {
			Log.e("Cloudsdale", e.getMessage());
		} catch (JSONException e) {
			Log.e("Cloudsdale", e.getMessage());
		}

		return null;
	}
	
	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		wrapper.postExecute(result);
	}

}
