package org.cloudsdale.android.models;

import java.util.ArrayList;

public class Cloud {

	// Object properties
	private String name;
	private String description;
	private boolean hidden;
	private boolean locked;
	private boolean featured;

	// Relationships
	private ArrayList<User> users;
	private Chat chat;
	private User owner;

	/**
	 * Default constructor
	 */
	public Cloud() {
		name = "";
		description = "";
		hidden = false;
		locked = false;
		featured = false;

		users = new ArrayList<User>();
		chat = new Chat();
		owner = null;
	}

	/**
	 * Basic cloud constructor
	 * 
	 * @param owner
	 *            the owner of the cloud
	 */
	public Cloud(User owner) {
		name = "";
		description = "";
		hidden = false;
		locked = false;
		featured = false;

		users = new ArrayList<User>();
		chat = new Chat();
		this.owner = owner;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the hidden
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * @param hidden
	 *            the hidden to set
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * @param locked
	 *            the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * @return the featured
	 */
	public boolean isFeatured() {
		return featured;
	}

	/**
	 * @param featured
	 *            the featured to set
	 */
	public void setFeatured(boolean featured) {
		this.featured = featured;
	}

	/**
	 * @return the users
	 */
	public ArrayList<User> getUsers() {
		return users;
	}

	/**
	 * @param users
	 *            the users to set
	 */
	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	/**
	 * @return the chat
	 */
	public Chat getChat() {
		return chat;
	}

	/**
	 * @return the owner
	 */
	public User getOwner() {
		return owner;
	}

	/**
	 * Adds a user to the cloud
	 * 
	 * @param user
	 *            the user to add
	 */
	public void addUser(User user) {
		users.add(user);
	}

	/**
	 * Remove a user from the cloud
	 * 
	 * @param user
	 *            the user to remove
	 */
	public void removeUser(User user) {
		if (users.contains(user)) {
			users.remove(user);
		}
	}

	/**
	 * Check to see if a user is a member of the cloud
	 * 
	 * @param user
	 *            the user to check
	 * @return whether the user is a member of the cloud
	 */
	public boolean checkIfUserIsMember(User user) {
		return users.contains(user);
	}
}
