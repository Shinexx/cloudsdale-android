package org.cloudsdale.logic;

import java.io.IOException;
import java.util.Date;

import org.cloudsdale.android.models.User;

import android.util.JsonReader;

public class UserJsonParser {
	
	@SuppressWarnings("deprecation")
	public User parseUserFromJson(JsonReader reader) {
		User user = new User();

		try {
			reader.beginObject();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if(name.equals("id"))
					user.setId(reader.nextString());
				else if(name.equals("name"))
					user.setName(reader.nextString());
				else if(name.equals("time_zone"))
					user.setTimeZone(reader.nextString());
				else if(name.equals("member_since"))
					user.setMemberSince(new Date(reader.nextString()));
				else if(name.equals("avatar"))
					// TODO parse avatar to model
					;
				else if(name.equals("clouds"))
					// TODO parse clouds into relations
					;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
}
