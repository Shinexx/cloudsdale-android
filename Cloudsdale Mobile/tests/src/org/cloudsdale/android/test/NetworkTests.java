package org.cloudsdale.android.test;

import android.test.ApplicationTestCase;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.models.network.SessionResponse;
import org.cloudsdale.android.models.network.UserResponse;
import org.cloudsdale.android.network.CloudsdaleApiClient;
import org.json.JSONObject;

/**
 * Unit test class to ensure API Client functionality <br/>
 * Copyright (c) 2012 Cloudsdale.org
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class NetworkTests extends ApplicationTestCase<Cloudsdale> {

	private CloudsdaleApiClient	client;
	private Gson				gson;

	public NetworkTests() {
		super(Cloudsdale.class);
	}

	@Override
	protected void setUp() throws Exception {
		createApplication();
		client = new CloudsdaleApiClient(getApplication());
		gson = getApplication().getJsonDeserializer();
		super.setUp();
	}

	public void testCloudsdaleSession() {
		client.getSession(Constants.DUMMY_EMAIL, Constants.DUMMY_PASSWORD,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(Throwable error, JSONObject response) {
						fail();

						super.onFailure(error, response);
					}

					@Override
					public void onSuccess(String json) {
						SessionResponse response = gson.fromJson(json,
								SessionResponse.class);

						// Make sure all relevant data is present
						assertNotNull(response);
						assertNotNull(response.getResult());
						assertNotNull(response.getResult().getClientId());
						assertNotNull(response.getResult().getUser());

						// Make sure we got the user we expected
						assertEquals(Constants.DUMMY_ID, response.getResult()
								.getUser().getId());
						assertEquals(Constants.DUMMY_EMAIL, response
								.getResult().getUser().getEmail());

						super.onSuccess(json);
					}

				});
	}

	public void testUserGet() {
		client.getUser(Constants.DUMMY_ID, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(Throwable error, JSONObject json) {
				fail();

				super.onFailure(error, json);
			}

			@Override
			public void onSuccess(String json) {
				UserResponse response = gson.fromJson(json, UserResponse.class);

				// Make sure all relevant data is present
				assertNotNull(response);
				assertNotNull(response.getResult());

				// Make sure we got the data we expected
				assertEquals(Constants.DUMMY_EMAIL, response.getResult()
						.getEmail());
				assertEquals(Constants.DUMMY_ID, response.getResult().getId());

				super.onSuccess(json);
			}

		});
	}
}
