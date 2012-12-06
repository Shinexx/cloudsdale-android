package org.cloudsdale.android.models.queries;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.api.Message;
import org.cloudsdale.android.models.exceptions.QueryException;
import org.cloudsdale.android.models.network.ApiMessageArrayResponse;

import java.io.IOException;

public class ChatMessageGetQuery extends GetQuery {

	public ChatMessageGetQuery(String url) {
		super(url);
	}

	private String		mJsonString;
	private Message[]	mResults;

	@Deprecated
	@Override
	public Message execute(QueryData data, Context context)
			throws QueryException {
		throw new UnsupportedOperationException(
				"Messages can only be queried for a collection");
	}

	@Override
	public Message[] executeForCollection(QueryData data, Context context)
			throws QueryException {
		addHeader("X-AUTH-TOKEN", Cloudsdale.getUserManager().getLoggedInUser()
				.getAuthToken());

		try {
			mHttpResponse = mhttpClient.execute(httpGet);

			mJsonString = EntityUtils.toString(mHttpResponse.getEntity());
			// mJsonString = stripHtml(mJsonString);
			if (Cloudsdale.isDebuggable()) {
				Log.d("ChatMessageGetQuery", mJsonString);
			}

			Gson gson = Cloudsdale.getJsonDeserializer();
			if (mJsonString != null) {
				ApiMessageArrayResponse resp = gson.fromJson(mJsonString,
						ApiMessageArrayResponse.class);
				if (resp.getStatus() == 200) {
					mResults = resp.getResult();
				} else {
					throw new QueryException(resp.getErrors()[0].getMessage(),
							resp.getStatus());
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return mResults;
	}
}
