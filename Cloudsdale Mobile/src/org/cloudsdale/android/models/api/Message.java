package org.cloudsdale.android.models.api;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.models.Model;

import java.util.Date;

public class Message extends Model<Message> {

    // Object attributes
	@Expose
    private Date     timestamp;
	@Expose
    private String   content;
	@Expose
    @SerializedName("author")
    private User     user;
	@Expose
    private String[] urls;
	@Expose
    private String   device;
	@Expose
    @SerializedName("author_id")
    private String   authorId;
	@Expose
    private Drop[]   drops;

    public Message() {
    	this(Cloudsdale.getContext());
    	this.timestamp = new Date();
    	this.content = "";
    }
    
    public Message(Context context) {
    	super(context);
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
