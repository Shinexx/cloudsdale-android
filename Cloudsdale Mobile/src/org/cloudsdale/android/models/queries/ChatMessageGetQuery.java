package org.cloudsdale.android.models.queries;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.api_models.Message;
import org.cloudsdale.android.models.network_models.ApiMessageArrayResponse;

import java.io.IOException;

public class ChatMessageGetQuery extends GetQuery {

    public ChatMessageGetQuery(String url) {
        super(url);
    }

    private String    mJsonString;
    private Message[] mResults;

    @Override
    public Message execute(QueryData data, Context context) {
        throw new UnsupportedOperationException(
                "Messages can only be queried for a collection");
    }

    @Override
    public Message[] executeForCollection(QueryData data, Context context) {
        setHeaders(data.getHeaders());

        try {
            mHttpResponse = mhttpClient.execute(httpGet);

            if (mHttpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) { return null; }

            mJsonString = EntityUtils.toString(mHttpResponse.getEntity());
//            mJsonString = stripHtml(mJsonString);
            if(Cloudsdale.DEBUG) {
                Log.d("ChatMessageGetQuery", mJsonString);
            }

            if (mJsonString != null) {
                Gson gson = new Gson();
                mResults = gson.fromJson(mJsonString,
                        ApiMessageArrayResponse.class).getResult();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mResults;
    }
}
