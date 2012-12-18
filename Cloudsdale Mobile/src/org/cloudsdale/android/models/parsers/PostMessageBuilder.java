package org.cloudsdale.android.models.parsers;

import com.google.gson.JsonObject;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.models.exceptions.QueryException;
import org.json.JSONException;

public class PostMessageBuilder {

	private JsonObject	message;

	public PostMessageBuilder(String message) throws JSONException {
		JsonObject body = new JsonObject();
		this.message = new JsonObject();
		try {
			body.addProperty("device", "mobile");
			body.addProperty("content", message);
			body.addProperty("client_id", Cloudsdale.getUserManager().getLoggedInUser().getId());
			this.message.addProperty("message", body.toString());
		} catch (QueryException e) {
			// TODO You dun goofed sending that shit, bitch
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return message.toString();
	}

}
