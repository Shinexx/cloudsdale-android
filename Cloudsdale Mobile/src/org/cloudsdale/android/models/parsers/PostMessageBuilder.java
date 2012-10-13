package org.cloudsdale.android.models.parsers;

import org.cloudsdale.android.managers.UserManager;
import org.json.JSONException;
import org.json.JSONObject;

public class PostMessageBuilder {

	private JSONObject	message;

	public PostMessageBuilder(String message) {
		JSONObject body = new JSONObject();
		this.message = new JSONObject();
		try {
			body.put("device", "mobile");
			body.put("content", message);
			body.put("client_id", UserManager.getLoggedInUser().getStringId());
			this.message.put("message", body.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return message.toString();
	}

}
