package org.cloudsdale.android.models.queries;

import com.bugsense.trace.BugSenseHandler;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.ClientProtocolException;
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
	public TwitterResponse execute(QueryData data) {
		TwitterResponse twitterResponse = null;

		try {
			// Sign the request and execute it
			oAuthConsumer.sign(httpGet);
			httpResponse = httpClient.execute(httpGet);

			// Deserialize the response
			JSONObject array = new JSONObject(httpResponse.getEntity().toString());
			String id = array.getString("id");
			twitterResponse = new TwitterResponse();
			twitterResponse.setId(Integer.valueOf(id));
		} catch (ClientProtocolException e) {
			BugSenseHandler.log(TAG, e);
		} catch (IOException e) {
			BugSenseHandler.log(TAG, e);
		} catch (OAuthMessageSignerException e) {
			BugSenseHandler.log(TAG, e);
		} catch (OAuthExpectationFailedException e) {
			BugSenseHandler.log(TAG, e);
		} catch (OAuthCommunicationException e) {
			BugSenseHandler.log(TAG, e);
		} catch (JSONException e) {
			BugSenseHandler.log(TAG, e);
		}

		return twitterResponse;
	}
}
