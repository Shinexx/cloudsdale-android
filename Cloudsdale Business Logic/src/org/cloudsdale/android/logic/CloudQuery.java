package org.cloudsdale.android.logic;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.cloudsdale.android.models.Cloud;
import org.cloudsdale.android.models.Response;

import android.os.AsyncTask;

import com.google.gson.Gson;

/**
 * Asynchronous query class to fetch clouds from Cloudsdale
 * 
 * @author Jamison Greeley
 * 
 */
public class CloudQuery extends AsyncTask<String, Void, Cloud[]> {
	// Final ints for indices
	public static final int	URL_INDEX	= 0;

	@Override
	protected Cloud[] doInBackground(String... params) {
		// Create and execute the post
		PostQueryObject post = new PostQueryObject(
				new ArrayList<NameValuePair>(), params[URL_INDEX]);
		String jsonResponse = post.execute();

		// create the Gson and parse the clouds
		Gson gson = new Gson();
		Response response = gson.fromJson(jsonResponse, Response.class);
		Cloud[] clouds = gson.fromJson(response.getResult(), Cloud[].class);

		return clouds;
	}

}
