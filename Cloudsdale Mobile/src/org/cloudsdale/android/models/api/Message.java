package org.cloudsdale.android.models.api;

import com.google.gson.annotations.SerializedName;

import org.cloudsdale.android.models.Model;

import java.util.Calendar;
import java.util.Date;

public class Message extends Model {

	// Object attributes
	@SerializedName("timestamp")
	private String		timestampTemp;
	private Calendar	timestampParsed;
	private String		content;
	@SerializedName("author")
	private User		user;
	private String[]	urls;
	private String		device;
	@SerializedName("author_id")
	private String		authorId;
	private Drop[]		drops;

	public Message() {
		this.content = "";
	}

	public User getAuthor() {
		return user;
	}

	public void setAuthor(User author) {
		this.user = author;
	}

	public String[] getUrls() {
		return urls;
	}

	public void setUrls(String[] urls) {
		this.urls = urls;
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
	public String getTimestampTemp() {
		return this.timestampTemp;
	}
	
	/**
	 * @return the timestamp
	 */
	public Calendar getTimestamp() {
		return this.timestampParsed;
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
	public void setTimestampTemp(String timestamp) {
		this.timestampTemp = timestamp;
		this.timestampParsed = convertDateString(timestamp);
	}
	
	public void setTimestamp(Calendar timestamp) {
		this.timestampTemp = convertCalendar(timestamp);
		this.timestampParsed = timestamp;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String id) {
		this.authorId = id;
	}

	public Drop[] getDrops() {
		return drops;
	}

	public void setDrops(Drop[] drops) {
		this.drops = drops;
	}
}
