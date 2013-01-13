package org.cloudsdale.android.test;

import android.test.AndroidTestCase;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.cloudsdale.android.network.CloudsdaleApiClient;
import org.json.JSONObject;

public class NetworkTests extends AndroidTestCase {

	public void testCloudsdaleSession() {
		CloudsdaleApiClient.getSession(Constants.DUMMY_EMAIL, Constants.DUMMY_PASSWORD, new JsonHttpResponseHandler() {
			
			@Override
			public void onFailure(Throwable error, JSONObject response) {
				fail();
				
				super.onFailure(error, response);
			}
			
			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(json);
			}
			
		});
	}

}
