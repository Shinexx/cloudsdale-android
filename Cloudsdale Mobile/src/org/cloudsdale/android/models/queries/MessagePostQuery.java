package org.cloudsdale.android.models.queries;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.api.Message;
import org.cloudsdale.android.models.exceptions.QueryException;
import org.cloudsdale.android.models.network.ApiMessageResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MessagePostQuery extends PostQuery {

    private String  mJsonString;
    private Message mResult;

    public MessagePostQuery(String url) {
        super(url);
    }

    @Override
    public Message execute(QueryData data, Context context)
            throws QueryException {
        try {
            mHttpPost.setEntity(new StringEntity(data.getJson()));
            setJsonContentType();
            mHttpResponse = mhttpClient.execute(mHttpPost);

            mJsonString = EntityUtils.toString(mHttpResponse.getEntity());
            if (Cloudsdale.DEBUG) {
                Log.d("Message Post", mJsonString);
            }
            if (mJsonString != null) {
                Gson gson = new Gson();
                ApiMessageResponse response = gson.fromJson(mJsonString,
                        ApiMessageResponse.class);
                if (response.getStatus() != 200) {
                    if (Cloudsdale.DEBUG) {
                        Log.d("Message Post",
                                response.getErrors()[0].getMessage());
                    }
                    throw new QueryException(
                            response.getErrors()[0].getMessage(),
                            response.getStatus());
                } else {
                    mResult = response.getResult();
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mResult;
    }

    /**
     * This method isn't used in this class and will only return null
     */
    @Deprecated
    @Override
    public Message[] executeForCollection(QueryData data, Context context) {
        throw new UnsupportedOperationException(
                "Message posting does not support simultaneous messages");
    }

}
