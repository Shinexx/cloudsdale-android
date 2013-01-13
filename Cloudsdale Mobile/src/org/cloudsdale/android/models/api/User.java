package org.cloudsdale.android.models.api;

import com.google.gson.annotations.SerializedName;

import org.cloudsdale.android.models.IdentityModel;

import java.util.ArrayList;
import java.util.Date;

import lombok.Data;

/**
 * User model for Cloudsdale
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
@Data
public class User extends IdentityModel {

	public static enum Role {

		//@formatter:off
		NORMAL(""),
		DONOR("[ donor ]"),
		LEGACY("[ legacy ]"),
		ASSOCIATE("[ associate ]"),
		ADMIN(""),
		DEVELOPER("[ dev ]"),
		FOUNDER("[ founder ]")
		;
		//@formatter:on

		private String	prettyName;

		Role(String prettyName) {
			this.prettyName = prettyName;
		}

		@Override
		public String toString() {
			return this.prettyName;
		}

	}

	protected final String		TAG	= "Cloudsdale User";

	// protected attributes from JSON
	protected String			name;
	protected String			email;
	@SerializedName("skype_name")
	protected String			skypeName;
	protected boolean			invisible;
	@SerializedName("time_zone")
	protected String			timeZone;
	@SerializedName("member_since")
	protected Date				memberSince;
	@SerializedName("suspended_until")
	protected Date				suspendedUntil;
	@SerializedName("reason_for_suspension")
	protected String			reasonForSuspension;
	protected Avatar			avatar;
	@SerializedName("is_registered")
	protected boolean			isRegistered;
	@SerializedName("is_transient")
	protected boolean			transientStatus;
	@SerializedName("is_banned")
	protected boolean			banStatus;
	@SerializedName("is_member_of_a_cloud")
	protected boolean			memberOfACloud;
	@SerializedName("has_an_avatar")
	protected boolean			hasAvatar;
	@SerializedName("has_read_tnc")
	protected boolean			hasReadTnC;
	@SerializedName("role")
	protected Role				userRole;
	protected Prosecution[]		prosecutions;
	@SerializedName("auth_token")
	protected String			authToken;
	@SerializedName("needs_to_confirm_registration")
	protected boolean			needsToConfirmRegistration;
	@SerializedName("needs_password_change")
	protected boolean			needsToChangePassword;
	@SerializedName("needs_name_change")
	protected boolean			needsToChangeName;
	@SerializedName("confirmed_registration_at")
	protected Date				confirmedRegistrationAt;
	@SerializedName("tnc_last_accepted")
	protected Date				tncLastAccepted;
	protected ArrayList<Cloud>	clouds;
	protected Ban[]				bans;

	public void setUserRole(Role role) {
		this.userRole = role;
	}

	public void addCloud(Cloud cloud) {
		clouds.add(cloud);
	}

	public void setClouds(ArrayList<Cloud> clouds) {
		this.clouds = clouds;
	}
	
}