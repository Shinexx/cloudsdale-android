package org.cloudsdale.android.models.queries;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.exceptions.CloudsdaleQueryException;
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

    @Deprecated
    @Override
    public Message execute(QueryData data, Context context)
            throws CloudsdaleQueryException {
        throw new UnsupportedOperationException(
                "Messages can only be queried for a collection");
    }

    @Override
    public Message[] executeForCollection(QueryData data, Context context)
            throws CloudsdaleQueryException {
        setHeaders(data.getHeaders());

        try {
            mHttpResponse = mhttpClient.execute(httpGet);

            mJsonString = EntityUtils.toString(mHttpResponse.getEntity());
            // mJsonString = stripHtml(mJsonString);
            if (Cloudsdale.DEBUG) {
                Log.d("ChatMessageGetQuery", mJsonString);
            }

            Gson gson = new Gson();
            if (mJsonString != null) {
                ApiMessageArrayResponse resp = gson.fromJson(mJsonString,
                        ApiMessageArrayResponse.class);
                if (resp.getStatus() == 200) {
                    mResults = resp.getResult();
                } else {
                    throw new CloudsdaleQueryException(
                            resp.getErrors()[0].getMessage(), resp.getStatus());
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
