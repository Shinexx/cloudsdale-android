package org.cloudsdale.model.v2;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tyr on 19/09/2013.
 */
public class Cloud extends BaseModel {

    @SerializedName("display_name")
    private String  displayName;
    @SerializedName("short_name")
    private String  shortName;
    private String  avatar;
    @SerializedName("participant_count")
    private int participantCount;
    private boolean hidden;
    private boolean locked;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }

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

}
