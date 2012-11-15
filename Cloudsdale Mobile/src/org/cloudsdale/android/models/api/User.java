package org.cloudsdale.android.models.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.models.AvatarContainer;
import org.cloudsdale.android.models.IdentityModel;
import org.cloudsdale.android.models.Role;

import com.google.gson.annotations.SerializedName;

/**
 * User model for Cloudsdale
 * 
 * @author Jamison Greeley (Berwyn@cloudsdale.org)
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends IdentityModel {

	protected final String				TAG	= "Cloudsdale User";

	// protected attributes from JSON
	protected String					name;
	protected String					email;
	protected String					skypeName;
	protected boolean					invisible;
	@SerializedName("time_zone")
	protected String					timeZone;
	@SerializedName("member_since")
	protected Date						memberSince;
	@SerializedName("suspended_until")
	protected Date						suspendedUntil;
	@SerializedName("reason_for_suspension")
	protected String					reasonForSuspension;
	protected AvatarContainer			avatar;
	@SerializedName("is_registered")
	protected boolean					isRegistered;
	@SerializedName("is_transient")
	protected boolean					transientStatus;
	@SerializedName("is_banned")
	protected boolean					banStatus;
	@SerializedName("is_member_of_a_cloud")
	protected boolean					memberOfACloud;
	@SerializedName("has_an_avatar")
	protected boolean					hasAvatar;
	@SerializedName("has_read_tnc")
	protected boolean					hasReadTnC;
	@SerializedName("role")
	protected Role						userRole;
	protected Prosecution[]				prosecutions;
	@SerializedName("auth_token")
	protected String					authToken;
	@SerializedName("needs_to_confirm_registration")
	protected boolean					needsToConfirmRegistration;
	@SerializedName("needs_password_change")
	protected boolean					needsToChangePassword;
	@SerializedName("needs_name_change")
	protected boolean					needsToChangeName;
	@SerializedName("confirmed_registration_at")
	protected Date						confirmedRegistrationAt;
	@SerializedName("tnc_last_accepted")
	protected Date						tncLastAccepted;
	@SerializedName("skype_name")
	protected HashMap<String, Cloud>	clouds;

	public void setUserRole(Role role) {
		this.userRole = role;
	}

	public void addCloud(Cloud cloud) {
		clouds.put(cloud.getId(), cloud);
	}

	public Cloud getCloud(String id) {
		return clouds.get(id);
	}

	public void convertClouds(ArrayList<Cloud> clouds) {
		for (Cloud c : clouds) {
			this.clouds.put(c.getId(), c);
		}
	}

	/**
	 * Converts the user to a JSON string
	 * 
	 * @return json representation of the user
	 */
	public String toJson() {
		return Cloudsdale.getJsonDeserializer().toJson(this, this.getClass());
	}

	/**
	 * Returns the string representation of the user
	 * 
	 * @see org.cloudsdale.android.models.api.User#toJson()
	 */
	@Override
	public String toString() {
		return toJson();
	}

}