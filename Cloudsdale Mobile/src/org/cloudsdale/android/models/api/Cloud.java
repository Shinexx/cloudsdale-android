package org.cloudsdale.android.models.api;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.cloudsdale.android.models.AvatarContainer;
import org.cloudsdale.android.models.IdentityModel;

import java.util.Calendar;

@DatabaseTable(tableName = "clouds")
public class Cloud extends IdentityModel {

    @DatabaseField
    private String          name;
    @DatabaseField
    private String          description;
    @DatabaseField
    @SerializedName("created_at")
    private String          createdTemp;
    private Calendar        createdAt;
    @DatabaseField
    private String          rules;
    @DatabaseField
    private boolean         hidden;
    @DatabaseField
    private AvatarContainer avatar;
    @DatabaseField
    @SerializedName("is_transient")
    private boolean         isTransient;
    @DatabaseField
    @SerializedName("owner")
    private String          ownerId;
    @DatabaseField
    @SerializedName("user_ids")
    private String[]        userIds;
    @DatabaseField
    @SerializedName("moderator_ids")
    private String[]        moderators;
    @DatabaseField
    @SerializedName("topic")
    private Chat            chat;

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
        this.createdAt = convertIsoString(createdTemp);
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
