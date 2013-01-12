package org.cloudsdale.android.test;

import java.util.Calendar;

import junit.framework.Assert;

import org.cloudsdale.android.models.LoggedUser;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.api.Drop;
import org.cloudsdale.android.models.api.Message;
import org.cloudsdale.android.models.api.User;
import org.cloudsdale.android.models.exceptions.QueryException;
import org.cloudsdale.android.models.queries.ChatMessageGetQuery;
import org.cloudsdale.android.models.queries.CloudGetQuery;
import org.cloudsdale.android.models.queries.DropGetQuery;
import org.cloudsdale.android.models.queries.MessagePostQuery;
import org.cloudsdale.android.models.queries.SessionQuery;
import org.cloudsdale.android.models.queries.UserGetQuery;
import org.json.JSONException;
import org.json.JSONObject;

import android.test.AndroidTestCase;
import android.util.Log;

import com.google.gson.JsonObject;

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
		QueryData data = setupQueryData(Constants.DUMMY_LOOKUP_ENDPOINT);
		LoggedUser session = establishSession();

		// Act
		UserGetQuery query = new UserGetQuery(data.getUrl());
		query.addHeader("X-AUTH-TOKEN", session.getAuthToken());
		User user = null;
		try {
			user = query.execute(data, null);
		} catch (QueryException e) {
			Assert.fail(e.getMessage());
		}

		// Assert
		Assert.assertNotNull(user);
		Assert.assertEquals("Cloudsdale Dummy Account", user.getName());
	}

	public void testCloudLookupQuery() {
		// Arrange
		QueryData data = setupQueryData(Constants.HAMMOCK_LOOKUP_ENDPOINT);
		LoggedUser session = establishSession();

		// Act
		CloudGetQuery query = new CloudGetQuery(data.getUrl());
		query.addHeader("X-AUTH-TOKEN", session.getAuthToken());
		Cloud cloud = null;
		try {
			cloud = query.execute(data, null);
		} catch (QueryException e) {
			Assert.fail(e.getMessage());
		}

		// Assert
		Assert.assertNotNull(cloud);
		Log.d("Cloud Lookup Test", cloud.getName());
		Assert.assertEquals("Hammock", cloud.getName());
	}

	public void testCloudChatQuery() {
		// Arrange
		QueryData data = setupQueryData(Constants.META_CHAT_ENDPOINT);
		LoggedUser session = establishSession();

		// Act
		ChatMessageGetQuery query = new ChatMessageGetQuery(data.getUrl());
		query.addHeader("X-AUTH-TOKEN", session.getAuthToken());
		Message[] messages = null;
		try {
			messages = query.executeForCollection(data, null);
		} catch (QueryException e) {
			Assert.fail(e.getMessage());
		}

		// Assert
		Assert.assertNotNull(messages);
	}

	public void testMessageSendQuery() {
		// Arrange
		QueryData data = setupQueryData(Constants.TEST_CHAT_ENDPOINT);
		LoggedUser session = establishSession();

		// Act
		MessagePostQuery query = new MessagePostQuery(data.getUrl());
		JSONObject body = new JSONObject();
		JSONObject message = new JSONObject();
		String content = "Testing, 123 testing! This is an automated unit test running on Android! BOING BOING! THE TIME IS NOW "
				+ Calendar.getInstance().getTime().toString().toUpperCase();
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
		Message result = null;
		try {
			result = query.execute(data, null);
		} catch (QueryException e) {
			Assert.fail(e.getMessage());
		}

		// Assert
		Log.d("Message Post Test", message.toString());
		Assert.assertNotNull(result);
		Assert.assertEquals(content, result.getContent());
	}

	public void testDropFetch() {
		// Arrange
		QueryData data = setupQueryData(Constants.META_DROP_ENDPOINT);
		LoggedUser session = establishSession();

		// Act
		DropGetQuery query = new DropGetQuery(data.getUrl());
		query.addHeader("X-AUTH-TOKEN", session.getAuthToken());
		Drop[] result;
		try {
			result = query.executeForCollection(data, null);
			// Assert
			Assert.assertNotNull(result);
			Assert.assertEquals(10, result.length);
			for (Drop drop : result) {
				Assert.assertNotNull(drop);
				Assert.assertNotNull(drop.getId());
				Assert.assertNotNull(drop.getPreview());
				Assert.assertNotNull(drop.getTitle());
				Assert.assertNotNull(drop.getUrl());
				Assert.assertNotNull(drop.getStatus());
				Assert.assertEquals("200", drop.getStatus()[0]);
				Assert.assertEquals("OK", drop.getStatus()[1]);
			}
		} catch (QueryException e) {
			fail(e.getMessage());
		}

	}

	private QueryData setupQueryData(String url) {
		QueryData data = new QueryData();
		data.setUrl(url);
		return data;
	}

	private QueryData setupQueryData_Username(String url) {
		QueryData data = setupQueryData(url);
		data.setUrl(Constants.SESSION_ENDPOINT);
		JsonObject json = new JsonObject();
		json.addProperty("email", Constants.DUMMY_EMAIL);
		json.addProperty("password", Constants.DUMMY_PASSWORD);
		data.setJson(json.toString());
		return data;
	}

	private LoggedUser establishSession() {
		QueryData data = setupQueryData_Username(Constants.SESSION_ENDPOINT);
		SessionQuery sq = new SessionQuery(data.getUrl());
		LoggedUser session;
		try {
			session = sq.execute(data, null);
		} catch (QueryException e) {
			Assert.fail(e.getMessage());
			return null;
		}
		return session;
	}
}
