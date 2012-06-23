package org.cloudsdale.android.models;

import java.text.ParseException;
import java.util.Calendar;

import com.bugsense.trace.BugSenseHandler;
import com.google.gson.annotations.SerializedName;

public class Cloud {

	private static final String	TAG	= "Cloudsdale Cloud Model";

	private String				id;
	private String				name;
	private String				description;
	@SuppressWarnings("unused")
	@SerializedName("created_at")
	private String				createdTemp;
	private Calendar			createdAt;
	private String				rules;
	private AvatarContainer		avatar;
	@SerializedName("is_transient")
	private boolean				isTransient;
	@SerializedName("owner")
	private User				ownerId;
	private User[]				moderators;
	private Chat				chat;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public String getRules() {
		return rules;
	}

	public AvatarContainer getAvatar() {
		return avatar;
	}

	public boolean isTransient() {
		return isTransient;
	}

	public User getOwnerId() {
		return ownerId;
	}

	public User[] getModerators() {
		return moderators;
	}

	public Chat getChat() {
		return chat;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCreatedTemp(String createdTemp) {
		try {
			createdAt = ISO8601.toCalendar(createdTemp);
		} catch (ParseException e) {
			BugSenseHandler.log(TAG, e);
		}
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public void setAvatar(AvatarContainer avatar) {
		this.avatar = avatar;
	}

	public void setTransient(boolean isTransient) {
		this.isTransient = isTransient;
	}

	public void setOwnerId(User ownerId) {
		this.ownerId = ownerId;
	}

	public void setModerators(User[] moderators) {
		this.moderators = moderators;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

}
