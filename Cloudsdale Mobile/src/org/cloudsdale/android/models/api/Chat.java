package org.cloudsdale.android.models.api;

import com.google.gson.annotations.Expose;

import org.cloudsdale.android.models.Model;

import java.util.ArrayList;

public class Chat extends Model {

	// Relationships
	@Expose
	private Cloud				cloud;
	@Expose
	private ArrayList<Message>	messages;
	@Expose
	private String				token;

	public ArrayList<Message> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<Message> messages) {
		this.messages = messages;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Default constructor
	 */
	public Chat() {
	}

	/**
	 * Parameterized constructor
	 * 
	 * @param cloud
	 *            cloud that this chat belongs to
	 */
	public Chat(Cloud cloud) {
		this.cloud = cloud;
	}

	public Cloud getCloud() {
		return this.cloud;
	}

	/**
	 * Set the cloud that this chat belongs to
	 * 
	 * @param cloud
	 *            the cloud that this chat belongs to
	 */
	public void setCloud(Cloud cloud) {
		this.cloud = cloud;
	}

}
