package org.cloudsdale.android.models.queries;

import android.content.Context;
import android.util.Log;

import com.bugsense.trace.BugSenseHandler;
import com.google.gson.Gson;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.api_models.Cloud;
import org.cloudsdale.android.models.network_models.ApiCloudArrayResponse;
import org.cloudsdale.android.models.network_models.ApiCloudResponse;

import java.io.IOException;

public class CloudGetQuery extends GetQuery {

    public CloudGetQuery(String url) {
        super(url);
    }

    private static final String TAG = "Cloud Get Query";

    private String              mJsonString;
    private Cloud[]             mResults;
    private Cloud               mResult;

    @Override
    public Cloud[] executeForCollection(QueryData data, Context context) {
        setHeaders(data.getHeaders());

        // Query the API
        try {
            // Get the response
            mHttpResponse = mhttpClient.execute(httpGet);

            // If we got anything other than a user, break out, there's
            // no point to continuing
            if (mHttpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) { return null; }

            // Build the json
            mJsonString = EntityUtils.toString(mHttpResponse.getEntity());
            mJsonString = stripHtml(mJsonString);

            // Deserialize
            Gson gson = new Gson();
            if (mJsonString != null) {
                mResults = gson.fromJson(mJsonString,
                        ApiCloudArrayResponse.class).getResult();
            }
        } catch (ClientProtocolException e) {
            BugSenseHandler.log(TAG, e);
        } catch (IOException e) {
            BugSenseHandler.log(TAG, e);
        }

        return mResults;
    }

    @Override
    public Cloud execute(QueryData data, Context context) {
        setupHttpObjects(data.getUrl());
        setHeaders(data.getHeaders());

        // Query the API
        try {
            // Get the response
            mHttpResponse = mhttpClient.execute(httpGet);

            // If we got anything other than a user, break out, there's
            // no point to continuing
            if (mHttpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) { return null; }

            // Build the json
            mJsonString = EntityUtils.toString(mHttpResponse.getEntity());
            mJsonString = stripHtml(mJsonString);

            // Deserialize
            Gson gson = new Gson();
            if (mJsonString != null) {
                Log.d(TAG, mJsonString);
                mResult = gson.fromJson(mJsonString, ApiCloudResponse.class)
                        .getResult();
            }
        } catch (ClientProtocolException e) {
            BugSenseHandler.log(TAG, e);
        } catch (IOException e) {
            BugSenseHandler.log(TAG, e);
        }

        return mResult;
    }

}
