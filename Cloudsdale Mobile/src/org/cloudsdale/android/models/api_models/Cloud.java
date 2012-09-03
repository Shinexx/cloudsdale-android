package org.cloudsdale.android.models.api_models;

import com.google.gson.annotations.SerializedName;

import org.cloudsdale.android.models.AvatarContainer;
import org.cloudsdale.android.models.IdentityModel;

import java.util.Calendar;

public class Cloud extends IdentityModel {

    private String          name;
    private String          description;
    @SerializedName("created_at")
    private String          createdTemp;
    private Calendar        createdAt;
    private String          rules;
    private AvatarContainer avatar;
    @SerializedName("is_transient")
    private boolean         isTransient;
    @SerializedName("owner")
    private User            ownerId;
    private User[]          moderators;
    @SerializedName("topic")
    private Chat            chat;

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    private boolean hidden;
    private boolean locked;
    private boolean featured;
    @SerializedName("member_count")
    private int     memberCount;

    public AvatarContainer getAvatar() {
        return this.avatar;
    }

    public Chat getChat() {
        return this.chat;
    }

    public Calendar getCreatedAt() {
        return this.createdAt;
    }

    public String getDescription() {
        return this.description;
    }

    public User[] getModerators() {
        return this.moderators;
    }

    public String getName() {
        return this.name;
    }

    public User getOwnerId() {
        return this.ownerId;
    }

    public String getRules() {
        return this.rules;
    }

    public boolean isTransient() {
        return this.isTransient;
    }

    public void setAvatar(AvatarContainer avatar) {
        this.avatar = avatar;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public void setCreatedAt(Calendar createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedTemp(String createdTemp) {
        this.createdAt = convertIsoString(createdTemp);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setModerators(User[] moderators) {
        this.moderators = moderators;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwnerId(User ownerId) {
        this.ownerId = ownerId;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public void setTransient(boolean isTransient) {
        this.isTransient = isTransient;
    }

}
