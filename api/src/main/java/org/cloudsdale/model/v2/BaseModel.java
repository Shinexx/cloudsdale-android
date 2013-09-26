package org.cloudsdale.model.v2;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by tyr on 19/09/2013.
 */
public abstract class BaseModel {

    private String  id;
    private Type    type;
    @SerializedName("created_at")
    private Date    createdAt;
    @SerializedName("updated_at")
    private Date    updatedAt;
    private boolean deleted;
    @SerializedName("transient")
    private boolean ofTransientStatus;
    @SerializedName("display_name")
    private String  displayName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isOfTransientStatus() {
        return ofTransientStatus;
    }

    public void setOfTransientStatus(boolean ofTransientStatus) {
        this.ofTransientStatus = ofTransientStatus;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
