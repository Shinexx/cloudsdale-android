package org.cloudsdale.android.models.queries;

import android.content.Context;

import com.bugsense.trace.BugSenseHandler;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.ClientProtocolException;
import org.cloudsdale.android.models.Model;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.network_models.TwitterResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class TwitterUserGetQuery extends GetQuery {

	private static final String			TAG	= "TwitterUserGetQuery";

	private CommonsHttpOAuthConsumer	oAuthConsumer;

	public TwitterUserGetQuery(String url,
			CommonsHttpOAuthConsumer oAuthConsumer) {
		setupHttpObjects(url);
		this.oAuthConsumer = oAuthConsumer;
	}

	@Override
	public TwitterResponse execute(QueryData data, Context context) {
		TwitterResponse twitterResponse = null;

		try {
			// Sign the request and execute it
			this.oAuthConsumer.sign(this.httpGet);
			this.httpResponse = this.httpClient.execute(this.httpGet);

			// Deserialize the response
			JSONObject array = new JSONObject(this.httpResponse.getEntity()
					.toString());
			String id = array.getString("id");
			twitterResponse = new TwitterResponse();
			twitterResponse.setId(Integer.valueOf(id));
		} catch (ClientProtocolException e) {
			BugSenseHandler.log(TwitterUserGetQuery.TAG, e);
		} catch (IOException e) {
			BugSenseHandler.log(TwitterUserGetQuery.TAG, e);
		} catch (OAuthMessageSignerException e) {
			BugSenseHandler.log(TwitterUserGetQuery.TAG, e);
		} catch (OAuthExpectationFailedException e) {
			BugSenseHandler.log(TwitterUserGetQuery.TAG, e);
		} catch (OAuthCommunicationException e) {
			BugSenseHandler.log(TwitterUserGetQuery.TAG, e);
		} catch (JSONException e) {
			BugSenseHandler.log(TwitterUserGetQuery.TAG, e);
		}

		return twitterResponse;
	}

	/**
	 * Not implemented for twitter get requests, will always return null. Use
	 * {@link #execute(QueryData, Context)} instead
	 */
	@Deprecated
	@Override
	public Model[] executeForCollection(QueryData data, Context context) {
		// Stub, will never be used in this class
		return null;
	}
}
