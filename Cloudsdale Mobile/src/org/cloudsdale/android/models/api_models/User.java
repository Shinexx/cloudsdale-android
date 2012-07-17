package org.cloudsdale.android.models.api_models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.cloudsdale.android.models.AvatarContainer;
import org.cloudsdale.android.models.IdentityModel;
import org.cloudsdale.android.models.enums.Role;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * User model for Cloudsdale
 * 
 * @author Jamison Greeley (Berwyn@cloudsdale.org)
 */
public class User extends IdentityModel {

	private final String		TAG	= "Cloudsdale User";

	// Private attributes from JSON
	private String				name;
	@SerializedName("time_zone")
	private String				timeZone;
	@SerializedName("member_since")
	private String				memberSinceTemp;
	private Calendar			memberSince;
	@SerializedName("suspended_until")
	private String				suspendedUntilTemp;
	private Calendar			suspendedUntil;
	@SerializedName("reason_for_suspension")
	private String				reasonForSuspension;
	private AvatarContainer		avatar;
	@SerializedName("is_registered")
	private boolean				isRegistered;
	@SerializedName("is_transient")
	private boolean				transientStatus;
	@SerializedName("is_banned")
	private boolean				banStatus;
	@SerializedName("is_member_of_a_cloud")
	private boolean				memberOfACloud;
	@SerializedName("has_an_avatar")
	private boolean				hasAvatar;
	@SerializedName("has_read_tnc")
	private boolean				hasReadTnC;
	@SerializedName("role")
	private String				roleTemp;
	private Role				userRole;
	private Prosecution[]		prosecutions;
	@SerializedName("auth_token")
	private String				authToken;
	private String				email;
	@SerializedName("needs_to_confirm_registration")
	private boolean				needsToConfirmRegistration;
	@SerializedName("needs_password_change")
	private boolean				needsToChangePassword;
	@SerializedName("needs_name_change")
	private boolean				needsToChangeName;

	// Child objects from JSON
	private ArrayList<Cloud>	clouds;

	public String getAuthToken() {
		return this.authToken;
	}

	public AvatarContainer getAvatar() {
		return this.avatar;
	}

	public ArrayList<Cloud> getClouds() {
		return this.clouds;
	}

	public String getEmail() {
		return this.email;
	}

	public Calendar getMemberSince() {
		if (this.memberSince != null) {
			return this.memberSince;
		} else {
			return convertIsoString(this.memberSinceTemp);
		}
	}

	public String getMemberSinceTemp() {
		return this.memberSinceTemp;
	}

	public String getName() {
		return this.name;
	}

	public Prosecution[] getProsecutions() {
		return this.prosecutions;
	}

	public String getReasonForSuspension() {
		return this.reasonForSuspension;
	}

	public Role getRole() {
		if (this.userRole != null) {
			return this.userRole;
		} else {
			if (this.roleTemp.toLowerCase().equals("creator")) {
				this.userRole = Role.CREATOR;
			} else if (this.roleTemp.toLowerCase().equals("admin")) {
				this.userRole = Role.ADMIN;
			} else if (this.roleTemp.toLowerCase().equals("moderator")) {
				this.userRole = Role.MODERATOR;
			} else if (this.roleTemp.toLowerCase().equals("donor")) {
				this.userRole = Role.DONOR;
			} else {
				this.userRole = Role.NORMAL;
			}

			return this.userRole;
		}
	}

	public String getRoleTemp() {
		return this.roleTemp;
	}

	public Calendar getSuspendedUntil() {
		return this.suspendedUntil;
	}

	public String getTAG() {
		return this.TAG;
	}

	public String getTimeZone() {
		return this.timeZone;
	}

	public boolean isBanStatus() {
		return this.banStatus;
	}

	public boolean isHasAvatar() {
		return this.hasAvatar;
	}

	public boolean isHasReadTnC() {
		return this.hasReadTnC;
	}

	public boolean isMemberOfACloud() {
		return this.memberOfACloud;
	}

	public boolean isNeedsToChangeName() {
		return this.needsToChangeName;
	}

	public boolean isNeedsToChangePassword() {
		return this.needsToChangePassword;
	}

	public boolean isNeedsToConfirmRegistration() {
		return this.needsToConfirmRegistration;
	}

	public boolean isRegistered() {
		return this.isRegistered;
	}

	public boolean isTransientStatus() {
		return this.transientStatus;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public void setAvatar(AvatarContainer avatar) {
		this.avatar = avatar;
	}

	public void setBanStatus(boolean banStatus) {
		this.banStatus = banStatus;
	}

	public void setClouds(ArrayList<Cloud> clouds) {
		this.clouds = clouds;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setHasAvatar(boolean hasAvatar) {
		this.hasAvatar = hasAvatar;
	}

	public void setHasReadTnC(boolean hasReadTnC) {
		this.hasReadTnC = hasReadTnC;
	}

	public void setMemberOfACloud(boolean memberOfACloud) {
		this.memberOfACloud = memberOfACloud;
	}

	public void setMemberSince(Calendar memberSince) {
		this.memberSince = memberSince;
	}

	public void setMemberSinceTemp(String iso8601) {
		this.memberSinceTemp = iso8601;
		this.memberSince = convertIsoString(iso8601);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNeedsToChangeName(boolean needsToChangeName) {
		this.needsToChangeName = needsToChangeName;
	}

	public void setNeedsToChangePassword(boolean needsToChangePassword) {
		this.needsToChangePassword = needsToChangePassword;
	}

	public void setNeedsToConfirmRegistration(boolean needsToConfirmRegistration) {
		this.needsToConfirmRegistration = needsToConfirmRegistration;
	}

	public void setProsecutions(Prosecution[] prosecutions) {
		this.prosecutions = prosecutions;
	}

	public void setReasonForSuspension(String reasonForSuspension) {
		this.reasonForSuspension = reasonForSuspension;
	}

	public void setRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

	public void setRoleTemp(String roleTemp) {
		this.roleTemp = roleTemp;
		if (roleTemp.toLowerCase().equals("creator")) {
			this.userRole = Role.CREATOR;
		} else if (roleTemp.toLowerCase().equals("admin")) {
			this.userRole = Role.ADMIN;
		} else if (roleTemp.toLowerCase().equals("moderator")) {
			this.userRole = Role.MODERATOR;
		} else if (roleTemp.toLowerCase().equals("donor")) {
			this.userRole = Role.DONOR;
		} else {
			this.userRole = Role.NORMAL;
		}
	}

	public void setSuspendedUntil(Calendar suspendedUntil) {
		this.suspendedUntil = suspendedUntil;
	}

	public void setSuspendedUntilTemp(String iso8601) {
		this.suspendedUntil = convertIsoString(iso8601);
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public void setTransientStatus(boolean transientStatus) {
		this.transientStatus = transientStatus;
	}

	public void setUserRole(Role userRole) {
		this.userRole = userRole;
	}

	/**
	 * Converts the user to a JSON string
	 * 
	 * @return json representation of the user
	 */
	public String toJson() {
		return new Gson().toJson(this, this.getClass());
	}

	/**
	 * Returns the string representation of the user
	 * 
	 * @see org.cloudsdale.android.models.api_models.User#toJson()
	 */
	@Override
	public String toString() {
		return toJson();
	}

}