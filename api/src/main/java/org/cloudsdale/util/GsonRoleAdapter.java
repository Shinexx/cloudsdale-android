package org.cloudsdale.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.cloudsdale.model.v1.User;

import java.lang.reflect.Type;

public class GsonRoleAdapter implements JsonDeserializer<User.Role> {

	@Override
	public User.Role deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		String input = json.getAsString();
		return User.Role.valueOf(input.toUpperCase());
	}

}
