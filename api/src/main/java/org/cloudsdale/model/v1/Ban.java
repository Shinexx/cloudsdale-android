package org.cloudsdale.model.v1;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

/**
 * Ban object model.
 *  Copyright(c) 2012 Cloudsdale.org
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class Ban extends IdentityModel {

	@SerializedName("reason")
	protected String	reasonText;
    protected Date		due;
    @SerializedName("created_at")
    protected Date		createdAt;
    @SerializedName("updated_at")
    protected Date		updatedAt;
    @SerializedName("revoke")
    protected boolean	revoked;
    @SerializedName("enforcer_id")
    protected String	enforcerId;
    @SerializedName("offender_id")
    protected String	offenderId;
    @SerializedName("jurisdiction_id")
    protected String	jurisdictionId;
    @SerializedName("jurisdiction_type")
    protected String	jurisdictionType;
    @SerializedName("has_expired")
    protected boolean	expired;
    @SerializedName("is_active")
    protected boolean	active;
    @SerializedName("is_transient")
    protected boolean	transience;

    public boolean isTransience() {
        return transience;
    }

    public void setTransience(boolean transience) {
        this.transience = transience;
    }

    public String getReasonText() {
        return reasonText;
    }

    public void setReasonText(String reasonText) {
        this.reasonText = reasonText;
    }

    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
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

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public String getEnforcerId() {
        return enforcerId;
    }

    public void setEnforcerId(String enforcerId) {
        this.enforcerId = enforcerId;
    }

    public String getOffenderId() {
        return offenderId;
    }

    public void setOffenderId(String offenderId) {
        this.offenderId = offenderId;
    }

    public String getJurisdictionId() {
        return jurisdictionId;
    }

    public void setJurisdictionId(String jurisdictionId) {
        this.jurisdictionId = jurisdictionId;
    }

    public String getJurisdictionType() {
        return jurisdictionType;
    }

    public void setJurisdictionType(String jurisdictionType) {
        this.jurisdictionType = jurisdictionType;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
