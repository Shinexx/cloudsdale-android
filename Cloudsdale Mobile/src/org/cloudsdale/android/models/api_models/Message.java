package org.cloudsdale.android.models.api_models;

import org.cloudsdale.android.models.Model;

import java.util.Date;

public class Message extends Model {

	// Object attributes
	private Date	timestamp;
	private String	content;
	private String	username;
	private String	userPath;
	private String	userAvatar;
	private String	authorId;

	public Message() {
		this.timestamp = new Date();
		this.content = "";
		this.username = "";
		this.userPath = "";
		this.userAvatar = "";
		this.authorId = "";
	}

	/**
	 * @return the authorId
	 */
	public String getAuthorId() {
		return this.authorId;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return this.timestamp;
	}

	/**
	 * @return the userAvatar
	 */
	public String getUserAvatar() {
		return this.userAvatar;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * @return the userPath
	 */
	public String getUserPath() {
		return this.userPath;
	}

	/**
	 * @param authorId
	 *            the authorId to set
	 */
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @param userAvatar
	 *            the userAvatar to set
	 */
	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param userPath
	 *            the userPath to set
	 */
	public void setUserPath(String userPath) {
		this.userPath = userPath;
	}

}
