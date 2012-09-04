package org.cloudsdale.android.test;

import android.test.AndroidTestCase;
import android.util.Log;

import org.apache.http.message.BasicNameValuePair;
import org.cloudsdale.android.models.LoggedUser;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.api_models.Cloud;
import org.cloudsdale.android.models.api_models.Message;
import org.cloudsdale.android.models.api_models.User;
import org.cloudsdale.android.models.queries.ChatMessageGetQuery;
import org.cloudsdale.android.models.queries.CloudGetQuery;
import org.cloudsdale.android.models.queries.MessagePostQuery;
import org.cloudsdale.android.models.queries.SessionQuery;
import org.cloudsdale.android.models.queries.UserGetQuery;
import org.json.JSONException;
import org.json.JSONObject;

import junit.framework.Assert;

import java.util.ArrayList;

public class QueryTests extends AndroidTestCase {

    public void testSessionQuery_Username() {
        LoggedUser u = establishSession();

        // Assert
        Assert.assertNotNull(u);
        Assert.assertEquals("Cloudsdale Dummy Account", u.getName());
    }

    public void testSessionQuery_FB() {

    }

    public void testSessionQuery_TW() {

    }

    public void testUserLookupQuery() {
        // Arrange
        QueryData qd = setupQueryData_UserGet();
        LoggedUser u = establishSession();

        // Act
        UserGetQuery ugq = new UserGetQuery(qd.getUrl());
        ugq.addHeader("X-AUTH-TOKEN", u.getAuthToken());
        User user = ugq.execute(qd, null);

        // Assert
        Assert.assertNotNull(user);
        Assert.assertEquals("Cloudsdale Dummy Account", user.getName());
    }

    public void testCloudLookupQuery() {
        // Arrange
        QueryData qd = setupQueryData_CloudGet();
        LoggedUser u = establishSession();

        // Act
        CloudGetQuery cgq = new CloudGetQuery(qd.getUrl());
        cgq.addHeader("X-AUTH-TOKEN", u.getAuthToken());
        Cloud cloud = cgq.execute(qd, null);

        // Assert
        Assert.assertNotNull(cloud);
        Log.d("Cloud Lookup Test", cloud.getName());
        Assert.assertEquals("Hammock", cloud.getName());
    }

    public void testCloudChatQuery() {
        // Arrange
        QueryData qd = setupQueryData_CloudChat();
        LoggedUser u = establishSession();

        // Act
        ChatMessageGetQuery cq = new ChatMessageGetQuery(qd.getUrl());
        cq.addHeader("X-AUTH-TOKEN", u.getAuthToken());
        Message[] messages = cq.executeForCollection(qd, null);

        // Assert
        Assert.assertNotNull(messages);
    }

    public void testMessageSendQuery() {
        // Arrange
        QueryData qd = setupQueryData_MessageSend();
        LoggedUser u = establishSession();

        // Act
        MessagePostQuery pq = new MessagePostQuery(qd.getUrl());
        JSONObject body = new JSONObject();
        JSONObject message = new JSONObject();
        
        try {
            body.put("content", "Testing, 123 testing!");
            body.put("device", "mobile");
            body.put("client_id", u.getId());
            message.put("message", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        qd.setJson(message.toString());
        pq.addHeader("X-AUTH-TOKEN", u.getAuthToken());
        Message result = pq.execute(qd, null);

        // Assert
        Log.d("Message Post Test", message.toString());
        Assert.assertNotNull(result);
    }

    private QueryData setupQueryData_Username() {
        QueryData qd = new QueryData();
        qd.setUrl(Constants.SESSION_ENDPOINT);
        ArrayList<BasicNameValuePair> headers = new ArrayList<BasicNameValuePair>();
        headers.add(new BasicNameValuePair("email", Constants.DUMMY_EMAIL));
        headers.add(new BasicNameValuePair("password", Constants.DUMMY_PASSWORD));
        qd.setHeaders(headers);
        return qd;
    }

    private QueryData setupQueryData_UserGet() {
        QueryData qd = new QueryData();
        qd.setUrl(Constants.DUMMY_LOOKUP_ENDPOINT);
        return qd;
    }

    private QueryData setupQueryData_CloudGet() {
        QueryData qd = new QueryData();
        qd.setUrl(Constants.HAMMOCK_LOOKUP_ENDPOINT);
        return qd;
    }

    private QueryData setupQueryData_CloudChat() {
        QueryData qd = new QueryData();
        qd.setUrl(Constants.HAMMOCK_CHAT_ENDPOINT);
        return qd;
    }

    private QueryData setupQueryData_MessageSend() {
        QueryData qd = new QueryData();
        qd.setUrl(Constants.META_CHAT_ENDPOINT);
        return qd;
    }

    private LoggedUser establishSession() {
        QueryData uqd = setupQueryData_Username();
        SessionQuery sq = new SessionQuery(uqd.getUrl());
        LoggedUser u = sq.execute(uqd, null);
        return u;
    }
}
