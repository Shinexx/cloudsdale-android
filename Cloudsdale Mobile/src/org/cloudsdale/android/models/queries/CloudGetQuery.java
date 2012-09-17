package org.cloudsdale.android.models.queries;

import android.content.Context;
import android.util.Log;

import com.bugsense.trace.BugSenseHandler;
import com.google.gson.Gson;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.exceptions.CloudsdaleQueryException;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.api_models.Cloud;
import org.cloudsdale.android.models.network_models.ApiCloudArrayResponse;
import org.cloudsdale.android.models.network_models.ApiCloudResponse;
import org.cloudsdale.android.models.network_models.ApiResponse;

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
    public Cloud[] executeForCollection(QueryData data, Context context)
            throws CloudsdaleQueryException {
        setHeaders(data.getHeaders());

        // Query the API
        try {
            // Get the response
            mHttpResponse = mhttpClient.execute(httpGet);

            // Build the json
            mJsonString = EntityUtils.toString(mHttpResponse.getEntity());
            mJsonString = stripHtml(mJsonString);

            // Deserialize
            Gson gson = new Gson();
            if (mJsonString != null) {
                if (Cloudsdale.DEBUG) {
                    Log.d(TAG, mJsonString);
                }
                if (mHttpResponse.getStatusLine().getStatusCode() == 200) {
                    ApiCloudArrayResponse response = gson.fromJson(mJsonString,
                            ApiCloudArrayResponse.class);
                    mResults = response.getResult();
                } else {
                    ApiResponse response = gson.fromJson(mJsonString,
                            ApiResponse.class);
                    throw new CloudsdaleQueryException(
                            response.getErrors()[0].getMessage(),
                            response.getStatus());
                }
            }
        } catch (ClientProtocolException e) {
            BugSenseHandler.log(TAG, e);
        } catch (IOException e) {
            BugSenseHandler.log(TAG, e);
        }

        return mResults;
    }

    @Override
    public Cloud execute(QueryData data, Context context)
            throws CloudsdaleQueryException {
        setupHttpObjects(data.getUrl());
        setHeaders(data.getHeaders());

        // Query the API
        try {
            // Get the response
            mHttpResponse = mhttpClient.execute(httpGet);

            // Build the json
            mJsonString = EntityUtils.toString(mHttpResponse.getEntity());
            mJsonString = stripHtml(mJsonString);

            // Deserialize
            Gson gson = new Gson();
            if (mJsonString != null) {
                Log.d(TAG, mJsonString);
                ApiCloudResponse response = gson.fromJson(mJsonString,
                        ApiCloudResponse.class);
                if (response.getStatus() == 200) {
                    mResult = response.getResult();
                } else {
                    throw new CloudsdaleQueryException(
                            response.getErrors()[0].getMessage(),
                            response.getStatus());
                }
            }
        } catch (ClientProtocolException e) {
            BugSenseHandler.log(TAG, e);
        } catch (IOException e) {
            BugSenseHandler.log(TAG, e);
        }

        return mResult;
    }

}
