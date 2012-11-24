package org.cloudsdale.android.models.api;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.cloudsdale.android.models.IdentityModel;

import com.google.gson.annotations.SerializedName;

/**
 * Ban object model.
 *  Copyright(c) 2012 Cloudsdale.org
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
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

}
