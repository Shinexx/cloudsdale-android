package org.cloudsdale.android.models;

import java.util.Date;

public class Message {

	// Object attributes
	private Date	timestamp;
	private String	content;
	private String	username;
	private String	userPath;
	private String	userAvatar;
	private String	authorId;

	public Message() {
		timestamp = new Date();
		content = "";
		username = "";
		userPath = "";
		userAvatar = "";
		authorId = "";
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the userPath
	 */
	public String getUserPath() {
		return userPath;
	}

	/**
	 * @param userPath
	 *            the userPath to set
	 */
	public void setUserPath(String userPath) {
		this.userPath = userPath;
	}

	/**
	 * @return the userAvatar
	 */
	public String getUserAvatar() {
		return userAvatar;
	}

	/**
	 * @param userAvatar
	 *            the userAvatar to set
	 */
	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	/**
	 * @return the authorId
	 */
	public String getAuthorId() {
		return authorId;
	}

	/**
	 * @param authorId
	 *            the authorId to set
	 */
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

}
