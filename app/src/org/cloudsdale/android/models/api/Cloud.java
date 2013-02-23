package org.cloudsdale.android.models.api;

import com.google.gson.annotations.SerializedName;

import org.cloudsdale.android.models.IdentityModel;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
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

}
