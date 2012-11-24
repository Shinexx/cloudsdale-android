package org.cloudsdale.android.models.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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

	protected final String						TAG	= "Cloudsdale User";

	// protected attributes from JSON
	protected String							name;
	protected String							email;
	@SerializedName("skype_name")
	protected String							skypeName;
	protected boolean							invisible;
	@SerializedName("time_zone")
	protected String							timeZone;
	@SerializedName("member_since")
	protected Date								memberSince;
	@SerializedName("suspended_until")
	protected Date								suspendedUntil;
	@SerializedName("reason_for_suspension")
	protected String							reasonForSuspension;
	protected AvatarContainer					avatar;
	@SerializedName("is_registered")
	protected boolean							isRegistered;
	@SerializedName("is_transient")
	protected boolean							transientStatus;
	@SerializedName("is_banned")
	protected boolean							banStatus;
	@SerializedName("is_member_of_a_cloud")
	protected boolean							memberOfACloud;
	@SerializedName("has_an_avatar")
	protected boolean							hasAvatar;
	@SerializedName("has_read_tnc")
	protected boolean							hasReadTnC;
	@SerializedName("role")
	protected Role								userRole;
	protected Prosecution[]						prosecutions;
	@SerializedName("auth_token")
	protected String							authToken;
	@SerializedName("needs_to_confirm_registration")
	protected boolean							needsToConfirmRegistration;
	@SerializedName("needs_password_change")
	protected boolean							needsToChangePassword;
	@SerializedName("needs_name_change")
	protected boolean							needsToChangeName;
	@SerializedName("confirmed_registration_at")
	protected Date								confirmedRegistrationAt;
	@SerializedName("tnc_last_accepted")
	protected Date								tncLastAccepted;
	protected ArrayList<Cloud>					clouds;
	protected Ban[]								bans;

	// Convenience objects
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	protected transient HashMap<String, Cloud>	mCloudLookupHash;
	

	public void setUserRole(Role role) {
		this.userRole = role;
	}

	public void addCloud(Cloud cloud) {
		clouds.add(cloud);
	}

	public Cloud getCloud(String id) {
		if (mCloudLookupHash.containsKey(id)) {
			return mCloudLookupHash.get(id);
		} else {
			Cloud output = null;
			for (Cloud c : clouds) {
				if (c.getId() == id) {
					mCloudLookupHash.put(id, c);
					output = c;
					break;
				}
			}
			return output;
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

}