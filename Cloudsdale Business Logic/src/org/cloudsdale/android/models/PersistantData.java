package org.cloudsdale.android.models;

import java.util.ArrayList;

public class PersistantData {

	private static User					Me;

	private static ArrayList<User>		users;
	private static ArrayList<Cloud>		clouds;
	private static ArrayList<Message>	messages;

	public static void setMe(User me) {
		Me = me;
	}

	public static User getMe() {
		return Me;
	}

	public static void storeUser(User user) {
		users.add(user);
	}

	public static User getUser(String userId) {
		for (User u : users) {
			if (u.getId() == userId) {
				return u;
			}
		}

		return null;
	}

	public static void storeCloud(Cloud cloud) {
		clouds.add(cloud);
	}

	public static Cloud getCloud(String cloudId) {
		for (Cloud c : clouds) {
			if (c.getId() == cloudId) {
				return c;
			}
		}

		return null;
	}

	public static void storeMessage(Message message) {
		messages.add(message);
	}

}
