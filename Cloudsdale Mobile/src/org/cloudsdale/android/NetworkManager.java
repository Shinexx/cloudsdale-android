package org.cloudsdale.android;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;
import org.cloudsdale.android.models.LoggedUser;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.authentication.Provider;
import org.cloudsdale.android.models.exceptions.QueryException;
import org.cloudsdale.android.models.queries.SessionQuery;

public class NetworkManager {

	private static LoggedUser sLoggedUser;
	private static QueryException sExceptionThrown;

	/*
	 * Public method to authenticate users via Cloudsdale credentials
	 */
	public static LoggedUser authenticate(String email, String password)
			throws QueryException {
		// TODO authenticate using Cloudsdale credentials
		// Reset the exception
		sExceptionThrown = null;

		// Get the api endpoint
		final String apiBase = Cloudsdale.getContext().getString(
				R.string.cloudsdale_api_base);
		final String sessionEndpoint = Cloudsdale.getContext().getString(
				R.string.cloudsdale_sessions_endpoint);
		final String apiUrl = apiBase + sessionEndpoint;

		// Build the query data
		final QueryData data = new QueryData();
		data.setUrl(apiUrl);
		ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(
				2);
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		data.setHeaders(nameValuePairs);

		// Execute the query
		new Thread() {
			public void run() {
				SessionQuery query = new SessionQuery(apiUrl);
				try {
					sLoggedUser = query.execute(data, Cloudsdale.getContext());
				} catch (QueryException e) {
					sExceptionThrown = e;
				}
			};
		}.start();
		if (sExceptionThrown != null) {
			return sLoggedUser;
		} else {
			throw sExceptionThrown;
		}
	}

	/*
	 * Public method to authenticate users via oAuth credentials (e.g. Twitter,
	 * Facebook)
	 */
	public static LoggedUser authenticate(String oAuthId, Provider oAuthProvider) {
		// TODO authenticate using oAuth credentials
		return null;
	}

}
