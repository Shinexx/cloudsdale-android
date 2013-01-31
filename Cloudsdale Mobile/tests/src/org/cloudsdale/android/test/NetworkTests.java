package org.cloudsdale.android.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.test.ApplicationTestCase;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.models.network.BanResponse;
import org.cloudsdale.android.models.network.CloudResponse;
import org.cloudsdale.android.models.network.DropResponse;
import org.cloudsdale.android.models.network.MessageResponse;
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

	private static CloudsdaleApiClient	client;
	private static Gson					gson;
	private static String				response;

	public NetworkTests() {
		super(Cloudsdale.class);
	}

	@Override
	protected void setUp() throws Exception {
		createApplication();
		client = new CloudsdaleApiClient(getApplication());
		gson = getApplication().getJsonDeserializer();
		response = null;
		super.setUp();
	}

	/**
	 * Test establishing a session via Cloudsdale credentials
	 */
	public void testCloudsdaleSession() {
		final CountDownLatch signal = new CountDownLatch(1);
		client.getSession(Constants.DUMMY_EMAIL, Constants.DUMMY_PASSWORD,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(Throwable error, String response) {
						response = null;

						super.onFailure(error, response);
						signal.countDown();
					}

					@Override
					public void onSuccess(String json) {
						response = json;

						super.onSuccess(json);
						signal.countDown();
					}

				});

		try {
			signal.await(10, TimeUnit.SECONDS);
			assertNotNull(this.response);
			SessionResponse response = gson.fromJson(this.response,
					SessionResponse.class);

			// Make sure all relevant data is present
			assertNotNull(response);
			assertNotNull(response.getResult());
			assertNotNull(response.getResult().getClientId());
			assertNotNull(response.getResult().getUser());

			// Make sure we got the user we expected
			assertEquals(Constants.DUMMY_ID, response.getResult().getUser()
					.getId());
			assertEquals(Constants.DUMMY_EMAIL, response.getResult().getUser()
					.getEmail());
		} catch (InterruptedException e) {
			fail();
		}
	}

	/**
	 * Test fetching a user via ID
	 */
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

	/**
	 * Test fetching clouds from the User-embedded endpoint
	 */
	public void testUserCloudsGet() {
		client.getUserClouds(Constants.DUMMY_ID, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(Throwable error, JSONObject json) {
				fail();

				super.onFailure(error, json);
			}

			@Override
			public void onSuccess(String json) {
				CloudResponse response = gson.fromJson(json,
						CloudResponse.class);

				// Make sure all relevant data is present
				assertNotNull(response);
				assertNotNull(response.getResults());

				// Make sure we got the data we expected
				assertTrue(response.getResults().size() > 0);

				super.onSuccess(json);
			}

		});
	}

	public void testCloudGet() {
		client.getCloud(Constants.HAMMOCK_ID, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(Throwable error, JSONObject json) {
				fail();

				super.onFailure(error, json);
			}

			@Override
			public void onSuccess(String json) {
				CloudResponse response = gson.fromJson(json,
						CloudResponse.class);

				// Make sure all relevant data is present
				assertNotNull(response);
				assertNotNull(response.getResult());

				// Make sure we got the data we expected
				assertEquals(Constants.HAMMOCK_SHORT_NAME, response.getResult()
						.getShortName());
				assertEquals(Constants.HAMMOCK_ID, response.getResult().getId());

				super.onSuccess(json);
			}

		});
	}

	public void testCloudBansGet() {
		client.getCloudBans(Constants.HAMMOCK_ID,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(Throwable error, JSONObject json) {
						fail();

						super.onFailure(error, json);
					}

					@Override
					public void onSuccess(String json) {
						BanResponse response = gson.fromJson(json,
								BanResponse.class);

						// Make sure all relevant data is present
						assertNotNull(response);
						assertNotNull(response.getResults());

						// Make sure we got the data we expected
						assertTrue(response.getResults().size() > 0);

						super.onSuccess(json);
					}

				});
	}

	public void testCloudDropsGet() {
		client.getCloudDrops(Constants.HAMMOCK_ID,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(Throwable error, JSONObject json) {
						fail();

						super.onFailure(error, json);
					}

					@Override
					public void onSuccess(String json) {
						DropResponse response = gson.fromJson(json,
								DropResponse.class);

						// Make sure all relevant data is present
						assertNotNull(response);
						assertNotNull(response.getResults());

						// Make sure we got the data we expected
						assertTrue(response.getResults().size() > 0);

						super.onSuccess(json);
					}

				});
	}

	public void testCloudMessagesGet() {
		client.getCloudMessages(Constants.HAMMOCK_ID,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(Throwable error, JSONObject json) {
						fail();

						super.onFailure(error, json);
					}

					@Override
					public void onSuccess(String json) {
						MessageResponse response = gson.fromJson(json,
								MessageResponse.class);

						// Make sure all relevant data is present
						assertNotNull(response);
						assertNotNull(response.getResults());

						// Make sure we got the data we expected
						assertTrue(response.getResults().size() > 0);

						super.onSuccess(json);
					}

				});
	}

	public void testCloudPopularGet() {
		client.getCloudPopular(new JsonHttpResponseHandler() {

			@Override
			public void onFailure(Throwable error, JSONObject json) {
				fail();

				super.onFailure(error, json);
			}

			@Override
			public void onSuccess(String json) {
				CloudResponse response = gson.fromJson(json,
						CloudResponse.class);

				// Make sure all relevant data is present
				assertNotNull(response);
				assertNotNull(response.getResults());

				// Make sure we got the data we expected
				assertTrue(response.getResults().size() > 0);

				super.onSuccess(json);
			}

		});
	}

	public void testCloudRecentGet() {
		client.getCloudRecents(new JsonHttpResponseHandler() {

			@Override
			public void onFailure(Throwable error, JSONObject json) {
				fail();

				super.onFailure(error, json);
			}

			@Override
			public void onSuccess(String json) {
				CloudResponse response = gson.fromJson(json,
						CloudResponse.class);

				// Make sure all relevant data is present
				assertNotNull(response);
				assertNotNull(response.getResults());

				// Make sure we got the data we expected
				assertTrue(response.getResults().size() > 0);

				super.onSuccess(json);
			}

		});
	}

	public void testCloudSearchPost() {
		client.postCloudSearch("Hammock", new JsonHttpResponseHandler() {

			@Override
			public void onFailure(Throwable error, JSONObject json) {
				fail();

				super.onFailure(error, json);
			}

			@Override
			public void onSuccess(String json) {
				CloudResponse response = gson.fromJson(json,
						CloudResponse.class);

				// Make sure all relevant data is present
				assertNotNull(response);
				assertNotNull(response.getResults());

				// Make sure we got the data we expected
				assertTrue(response.getResults().size() > 0);

				super.onSuccess(json);
			}

		});
	}
}
