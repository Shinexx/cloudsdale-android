package org.cloudsdale.android.models.api_models;

import com.google.gson.annotations.SerializedName;

import org.cloudsdale.android.models.Model;

import java.util.Date;

public class Message extends Model {

    // Object attributes
    private Date     timestamp;
    private String   content;
    @SerializedName("author")
    private User     user;
    private String[] urls;
    private String   device;
    @SerializedName("author_id")
    private String   authorId;
    private Drop[]   drops;

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

    public Message() {
        this.timestamp = new Date();
        this.content = "";
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
