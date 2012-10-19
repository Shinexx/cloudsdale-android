package org.cloudsdale.android.models.api;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.models.AvatarContainer;
import org.cloudsdale.android.models.IdentityModel;

import java.util.Calendar;

public class Cloud extends IdentityModel<Cloud> {

    @Expose
    private String          name;
    @Expose
    private String          description;
    @Expose
    @SerializedName("created_at")
    private String          createdTemp;
    private Calendar        createdAt;
    @Expose
    private String          rules;
    @Expose
    private boolean         hidden;
    @Expose
    private AvatarContainer avatar;
    @Expose
    @SerializedName("is_transient")
    private boolean         isTransient;
    @Expose
    @SerializedName("owner")
    private String          ownerId;
    @Expose
    @SerializedName("user_ids")
    private String[]        userIds;
    @Expose
    @SerializedName("moderator_ids")
    private String[]        moderators;
    @Expose
    @SerializedName("topic")
    private Chat            chat;
    
    public Cloud() {
    	this(Cloudsdale.getContext());
    }
    
    public Cloud(Context context) {
    	super(context);
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

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

    public String[] getModerators() {
        return this.moderators;
    }

    public String getName() {
        return this.name;
    }

    public String getOwnerId() {
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
        this.createdAt = convertDateString(createdTemp);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setModerators(String[] moderators) {
        this.moderators = moderators;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public void setTransient(boolean isTransient) {
        this.isTransient = isTransient;
    }

}
