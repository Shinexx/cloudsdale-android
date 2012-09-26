package org.cloudsdale.android.models.queries;

import android.content.Context;
import android.util.Log;

import com.bugsense.trace.BugSenseHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.cloudsdale.android.models.LoggedUser;
import org.cloudsdale.android.models.Model;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.annotations.GsonIgnoreExclusionStrategy;
import org.cloudsdale.android.models.exceptions.QueryException;
import org.cloudsdale.android.models.exceptions.ExternalServiceException;
import org.cloudsdale.android.models.exceptions.NotAuthorizedException;
import org.cloudsdale.android.models.network.LoginResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Query runner class that asynchronously establishes a session with Cloudsdale.
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com) Copyright (C) 2012
 *         Cloudsdale.org, all rights reserved
 */
public class SessionQuery extends PostQuery {

    public SessionQuery(String url) {
        super(url);
    }

    private static final String TAG = "Session Query";

    private String              mJsonString;
    private LoggedUser          mUser;

    /**
     * Execute the query, establishing a session with Cloudsdale
     * 
     * @throws NotAuthorizedException
     * @throws ExternalServiceException
     * @throws QueryException
     */
    @Override
    public LoggedUser execute(QueryData data, Context context)
            throws QueryException {
        // Set the entity
        if (data.getHeaders() != null) {
            try {
                this.mHttpPost.setEntity(new UrlEncodedFormEntity(data
                        .getHeaders()));
            } catch (UnsupportedEncodingException e) {
                BugSenseHandler.log(SessionQuery.TAG, e);
            }
        } else if (data.getJson() != null) {
            try {
                this.mHttpPost.setEntity(new StringEntity(data.getJson()));
            } catch (UnsupportedEncodingException e) {
                BugSenseHandler.log(SessionQuery.TAG, e);
            }
        }

        // Query the API
        try {
            // Get the response
            mHttpResponse = mhttpClient.execute(mHttpPost);

            // Build the json
            mJsonString = EntityUtils.toString(mHttpResponse.getEntity());

            // [DEBUG] Logcat the json response
            Log.d(SessionQuery.TAG, "Session API response: " + mJsonString);

            // Deserialize
            Gson gson = new Gson();
            if (this.mJsonString != null) {
                LoginResponse resp = gson.fromJson(mJsonString,
                        LoginResponse.class);
                if (resp.getStatus() == 200) {
                    mUser = (LoggedUser) resp.getResult().getUser();
                    mUser.setClientId(resp.getResult().getClientId());
                } else {
                    throw new QueryException(
                            resp.getErrors()[0].getMessage(), resp.getStatus());
                }
            }
        } catch (ClientProtocolException e) {
            Log.d(SessionQuery.TAG, "Client Protocol Exception");
            BugSenseHandler.log(SessionQuery.TAG, e);
        } catch (IOException e) {
            Log.d(SessionQuery.TAG, "IO Exception");
            BugSenseHandler.log(SessionQuery.TAG, e);
        }

        Log.d(SessionQuery.TAG, "User: "
                + (this.mUser == null ? "Null" : "Not Null"));

        return this.mUser;
    }

    /**
     * Not implemented for sessions, will always return null. Use
     * {@link #execute(QueryData, Context)} instead
     */
    @Deprecated
    @Override
    public Model[] executeForCollection(QueryData data, Context context)
            throws QueryException {
        throw new UnsupportedOperationException(
                "Cannot execute session queries for collections");
    }
}
