package org.cloudsdale.android.logic;

import android.os.AsyncTask;

/**
 * Query class to fetch clouds from Cloudsdale
 * 
 * @author Jamison Greeley
 * 
 */
public class CloudQuery extends AsyncTask<String, Void, String>{
	// Final indexes for passed information
	private static final int ID_TYPE_INDEX = 0;
	private static final int ID_INDEX = 1;
	private static final int URL_INDEX = 2;
	
	// Final strings for id types
	private static final String USER_ID_TYPE = "userid";
	private static final String CLOUD_ID_TYPE = "cloudid";

	@Override
	protected String doInBackground(String... params) {
		// TODO Create query logic for fetching clouds
		return null;
	}
	
}
