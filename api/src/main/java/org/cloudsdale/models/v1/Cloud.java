package org.cloudsdale.models.v1;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Cloud extends IdentityModel {

	private String		name;
	private String		description;
	@SerializedName("created_at")
	private Date		createdAt;
	private String		rules;
	private boolean		hidden;
	private Avatar		avatar;
	@SerializedName("is_transient")
	private boolean		isTransient;
	@SerializedName("owner")
	private String		ownerId;
	@SerializedName("user_ids")
	private String[]	userIds;
	@SerializedName("moderator_ids")
	private String[]	moderators;
	@SerializedName("topic")
	private Chat		chat;
	@SerializedName("short_name")
	private String		shortName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public boolean isTransient() {
        return isTransient;
    }

    public void setTransient(boolean aTransient) {
        isTransient = aTransient;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String[] getUserIds() {
        return userIds;
    }

    public void setUserIds(String[] userIds) {
        this.userIds = userIds;
    }

    public String[] getModerators() {
        return moderators;
    }

    public void setModerators(String[] moderators) {
        this.moderators = moderators;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
