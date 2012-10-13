package org.cloudsdale.android.models.queries;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.managers.UserManager;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.api.Drop;
import org.cloudsdale.android.models.network.ApiDropArrayResponse;

import java.io.IOException;

public class DropGetQuery extends GetQuery {

    private String mJsonString;
    private Drop[] mResults;

    public DropGetQuery(String url) {
        super(url);
    }

    /**
     * This method is currently not supported, as searching drops by ID is not
     * supported
     */
    @Deprecated
    @Override
    public Drop execute(QueryData data, Context context) {
        throw new UnsupportedOperationException(
                "The Cloudsdale v1 API does not support querying single drops");
    }

    @Override
    public Drop[] executeForCollection(QueryData data, Context context) {
    	addHeader("X-AUTH-TOKEN", UserManager.getLoggedInUser().getAuthToken());

        try {
            mHttpResponse = mhttpClient.execute(httpGet);

            if (mHttpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) { return null; }

            mJsonString = EntityUtils.toString(mHttpResponse.getEntity());
            if (Cloudsdale.DEBUG) {
                Log.d("DropFetchQuery", mJsonString);
            }

            if (mJsonString != null) {
                Gson gson = new Gson();
                mResults = gson.fromJson(mJsonString,
                        ApiDropArrayResponse.class).getResult();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mResults;
    }
}
