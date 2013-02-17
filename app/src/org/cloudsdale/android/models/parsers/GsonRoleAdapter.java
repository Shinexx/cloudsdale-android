package org.cloudsdale.android.models.parsers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.cloudsdale.android.models.api.User.Role;

import java.lang.reflect.Type;

public class GsonRoleAdapter implements JsonDeserializer<Role> {

	@Override
	public Role deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		String input = json.getAsString();
		return Role.valueOf(input.toUpperCase());
	}

}
