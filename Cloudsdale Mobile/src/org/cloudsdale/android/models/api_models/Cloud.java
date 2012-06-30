package org.cloudsdale.android.models.api_models;

import java.util.Calendar;

import org.cloudsdale.android.models.AvatarContainer;
import org.cloudsdale.android.models.IdentityModel;

import com.google.gson.annotations.SerializedName;

public class Cloud extends IdentityModel {

	private String				name;
	private String				description;
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

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCreatedTemp(String createdTemp) {
		createdAt = convertIsoString(createdTemp);
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
