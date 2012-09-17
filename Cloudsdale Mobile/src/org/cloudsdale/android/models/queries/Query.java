package org.cloudsdale.android.models.queries;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.cloudsdale.android.exceptions.CloudsdaleQueryException;
import org.cloudsdale.android.models.Model;
import org.cloudsdale.android.models.QueryData;

public abstract class Query {

    protected HttpClient   mhttpClient;
    protected HttpResponse mHttpResponse;
    protected String       mUrl;

    public Query(String url) {
        setupHttpObjects(url);
    }

    protected void setupHttpObjects(String url) {
        this.mhttpClient = new DefaultHttpClient();
    }

    public final String stripHtml(String htmlString) {
        return android.text.Html.fromHtml(htmlString).toString();
    }

    public abstract void addHeader(String key, String value);

    public abstract Model execute(QueryData data, Context context) throws CloudsdaleQueryException;

    public abstract Model[] executeForCollection(QueryData data, Context context) throws CloudsdaleQueryException;

}
