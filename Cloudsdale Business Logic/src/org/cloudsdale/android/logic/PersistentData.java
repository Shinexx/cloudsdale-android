package org.cloudsdale.android.logic;

import java.util.ArrayList;

import org.cloudsdale.android.models.Cloud;
import org.cloudsdale.android.models.Message;
import org.cloudsdale.android.models.User;

/**
 * Persistent Data object for Cloudsdale
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class PersistentData {

	// The logged in user
	private static User					Me;

	// Storage objects
	private static ArrayList<User>		users;
	private static ArrayList<Cloud>		clouds;
	private static ArrayList<Message>	messages;

	/**
	 * Set the logged in user
	 * 
	 * @param me
	 *            The logged in user
	 */
	public static void setMe(User me) {
		Me = me;
	}

	/**
	 * Get the logged in user
	 * 
	 * @return The logged in user
	 */
	public static User getMe() {
		return Me;
	}

	/**
	 * Store a user
	 * 
	 * @param user
	 *            A user to be stored
	 */
	public static void storeUser(User user) {
		users.add(user);
	}

	/**
	 * Get a user from storage
	 * 
	 * @param userId
	 *            The id of the user to find
	 * @return The user as identified by the id
	 */
	public static User getUser(String userId) {
		for (User u : users) {
			if (u.getId() == userId) {
				return u;
			}
		}

		return null;
	}

	/**
	 * Store a cloud
	 * 
	 * @param cloud
	 *            The cloud to be stored
	 */
	public static void storeCloud(Cloud cloud) {
		clouds.add(cloud);
	}

	/**
	 * Get a cloud from storage
	 * 
	 * @param cloudId
	 *            The id of the cloud to find
	 * @return The cloud as identified by the id
	 */
	public static Cloud getCloud(String cloudId) {
		for (Cloud c : clouds) {
			if (c.getId() == cloudId) {
				return c;
			}
		}

		return null;
	}

	/**
	 * Store a message
	 * 
	 * @param message
	 *            The message to be stored
	 */
	public static void storeMessage(Message message) {
		messages.add(message);
	}

}
