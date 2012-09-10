package org.cloudsdale.android.test;

import android.test.AndroidTestCase;
import android.util.Log;

import org.apache.http.message.BasicNameValuePair;
import org.cloudsdale.android.models.LoggedUser;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.api_models.Cloud;
import org.cloudsdale.android.models.api_models.Drop;
import org.cloudsdale.android.models.api_models.Message;
import org.cloudsdale.android.models.api_models.User;
import org.cloudsdale.android.models.queries.ChatMessageGetQuery;
import org.cloudsdale.android.models.queries.CloudGetQuery;
import org.cloudsdale.android.models.queries.DropGetQuery;
import org.cloudsdale.android.models.queries.MessagePostQuery;
import org.cloudsdale.android.models.queries.SessionQuery;
import org.cloudsdale.android.models.queries.UserGetQuery;
import org.json.JSONException;
import org.json.JSONObject;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Calendar;

public class QueryTests extends AndroidTestCase {

    public void testSessionQuery_Username() {
        LoggedUser user = establishSession();

        // Assert
        Assert.assertNotNull(user);
        Assert.assertEquals("Cloudsdale Dummy Account", user.getName());
    }

    public void testSessionQuery_FB() {

    }

    public void testSessionQuery_TW() {

    }

    public void testUserLookupQuery() {
        // Arrange
        QueryData data = setupQueryData_UserGet();
        LoggedUser session = establishSession();

        // Act
        UserGetQuery query = new UserGetQuery(data.getUrl());
        query.addHeader("X-AUTH-TOKEN", session.getAuthToken());
        User user = query.execute(data, null);

        // Assert
        Assert.assertNotNull(user);
        Assert.assertEquals("Cloudsdale Dummy Account", user.getName());
    }

    public void testCloudLookupQuery() {
        // Arrange
        QueryData data = setupQueryData_CloudGet();
        LoggedUser session = establishSession();

        // Act
        CloudGetQuery query = new CloudGetQuery(data.getUrl());
        query.addHeader("X-AUTH-TOKEN", session.getAuthToken());
        Cloud cloud = query.execute(data, null);

        // Assert
        Assert.assertNotNull(cloud);
        Log.d("Cloud Lookup Test", cloud.getName());
        Assert.assertEquals("Hammock", cloud.getName());
    }

    public void testCloudChatQuery() {
        // Arrange
        QueryData data = setupQueryData_CloudChat();
        LoggedUser session = establishSession();

        // Act
        ChatMessageGetQuery query = new ChatMessageGetQuery(data.getUrl());
        query.addHeader("X-AUTH-TOKEN", session.getAuthToken());
        Message[] messages = query.executeForCollection(data, null);

        // Assert
        Assert.assertNotNull(messages);
    }

    public void testMessageSendQuery() {
        // Arrange
        QueryData data = setupQueryData_MessageSend();
        LoggedUser session = establishSession();

        // Act
        MessagePostQuery query = new MessagePostQuery(data.getUrl());
        JSONObject body = new JSONObject();
        JSONObject message = new JSONObject();
        String content = "Testing, 123 testing! This is an automated unit test running on Android! BOING BOING! THE TIME IS NOW "
                + Calendar.getInstance().getTime().toString()
                .toUpperCase();
        try {
            body.put("content", content);
            body.put("device", "mobile");
            body.put("client_id", session.getId());
            message.put("message", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        data.setJson(message.toString());
        query.addHeader("X-AUTH-TOKEN", session.getAuthToken());
        Message result = query.execute(data, null);

        // Assert
        Log.d("Message Post Test", message.toString());
        Assert.assertNotNull(result);
        Assert.assertEquals(content, result.getContent());
    }
    
    public void testDropFetch() {
        // Arrange
        QueryData data = setupQueryData_DropFetch();
        LoggedUser session = establishSession();
        
        // Act
        DropGetQuery query = new DropGetQuery(data.getUrl());
        query.addHeader("X-AUTH-TOKEN", session.getAuthToken());
        Drop[] result = query.executeForCollection(data, null);
        
        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(10, result.length);
        for(Drop drop : result) {
            Assert.assertNotNull(drop);
            Assert.assertNotNull(drop.getId());
            Assert.assertNotNull(drop.getPreview());
            Assert.assertNotNull(drop.getTitle());
            Assert.assertNotNull(drop.getUrl());
            Assert.assertNotNull(drop.getStatus());
            Assert.assertEquals("200", drop.getStatus()[0]);
            Assert.assertEquals("OK", drop.getStatus()[1]);
        }
    }

    private QueryData setupQueryData_Username() {
        QueryData data = new QueryData();
        data.setUrl(Constants.SESSION_ENDPOINT);
        ArrayList<BasicNameValuePair> headers = new ArrayList<BasicNameValuePair>();
        headers.add(new BasicNameValuePair("email", Constants.DUMMY_EMAIL));
        headers.add(new BasicNameValuePair("password", Constants.DUMMY_PASSWORD));
        data.setHeaders(headers);
        return data;
    }

    private QueryData setupQueryData_UserGet() {
        QueryData data = new QueryData();
        data.setUrl(Constants.DUMMY_LOOKUP_ENDPOINT);
        return data;
    }

    private QueryData setupQueryData_CloudGet() {
        QueryData data = new QueryData();
        data.setUrl(Constants.HAMMOCK_LOOKUP_ENDPOINT);
        return data;
    }

    private QueryData setupQueryData_CloudChat() {
        QueryData data = new QueryData();
        data.setUrl(Constants.HAMMOCK_CHAT_ENDPOINT);
        return data;
    }

    private QueryData setupQueryData_MessageSend() {
        QueryData data = new QueryData();
        data.setUrl(Constants.TEST_CHAT_ENDPOINT);
        return data;
    }
    
    private QueryData setupQueryData_DropFetch() {
        QueryData data = new QueryData();
        data.setUrl(Constants.META_DROP_ENDPOINT);
        return data;
    }

    private LoggedUser establishSession() {
        QueryData data = setupQueryData_Username();
        SessionQuery sq = new SessionQuery(data.getUrl());
        LoggedUser session = sq.execute(data, null);
        return session;
    }
}
