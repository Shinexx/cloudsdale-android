package org.cloudsdale.android.models.api;

import java.util.Date;

import org.cloudsdale.android.models.Model;

import com.google.gson.annotations.SerializedName;

public class Message extends Model {

	private Date		timestamp;
	private String		content;
	private User		author;
	private String[]	urls;
	private String		device;
	@SerializedName("author_id")
	private String		authorId;
	private Drop[]		drops;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
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

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Drop[] getDrops() {
        return drops;
    }

    public void setDrops(Drop[] drops) {
        this.drops = drops;
    }
}
