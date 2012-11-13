package org.cloudsdale.android.models.api;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

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
public class User extends IdentityModel {

	protected final String				TAG	= "Cloudsdale User";

	// protected attributes from JSON
	protected String					name;
	@SerializedName("time_zone")
	protected String					timeZone;
	@SerializedName("member_since")
	protected String					memberSinceTemp;
	protected Calendar					memberSince;
	@SerializedName("suspended_until")
	protected String					suspendedUntilTemp;
	protected Calendar					suspendedUntil;
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
	protected String					roleTemp;
	protected Role						userRole;
	protected Prosecution[]				prosecutions;
	@SerializedName("auth_token")
	protected String					authToken;
	protected String					email;
	@SerializedName("needs_to_confirm_registration")
	protected boolean					needsToConfirmRegistration;
	@SerializedName("needs_password_change")
	protected boolean					needsToChangePassword;
	@SerializedName("needs_name_change")
	protected boolean					needsToChangeName;
	@SerializedName("confirmed_registration_at")
	protected String					confirmedRegistrationAtTemp;
	protected Calendar					confirmedRegistrationAt;
	@SerializedName("tnc_last_accepted")
	protected String					tncLastAcceptedTemp;
	protected Calendar					tncLastAccepted;
	@SerializedName("skype_name")
	protected String					skypeName;

	// Child objects from JSON
	protected HashMap<String, Cloud>	clouds;

	public String getAuthToken() {
		return this.authToken;
	}

	public AvatarContainer getAvatar() {
		return this.avatar;
	}

	public ArrayList<Cloud> getClouds() {
		if (clouds == null) {
			clouds = new HashMap<String, Cloud>();
		}
		return new ArrayList<Cloud>(clouds.values());
	}

	public String getEmail() {
		return this.email;
	}

	public Calendar getMemberSince() {
		if (this.memberSince != null) {
			return this.memberSince;
		} else {
			return convertDateString(this.memberSinceTemp);
		}
	}

	public String getMemberSinceTemp() {
		return this.memberSinceTemp;
	}

	public Calendar getConfirmedRegistrationAt() {
		if (this.confirmedRegistrationAt != null) {
			return this.confirmedRegistrationAt;
		} else {
			return convertDateString(this.confirmedRegistrationAtTemp);
		}
	}

	public String getConfirmedRegistrationAtTemp() {
		return confirmedRegistrationAtTemp;
	}

	public Calendar getTncLastAccpeted() {
		if (this.tncLastAccepted != null) {
			return this.tncLastAccepted;
		} else {
			return convertDateString(this.tncLastAcceptedTemp);
		}
	}

	public String getTncLastAcceptedTemp() {
		return tncLastAcceptedTemp;
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
			if (this.roleTemp.toLowerCase().equals("founder")) {
				this.userRole = Role.FOUNDER;
			} else if (this.roleTemp.toLowerCase().equals("admin")) {
				this.userRole = Role.ADMIN;
			} else if (this.roleTemp.toLowerCase().equals("moderator")) {
				this.userRole = Role.MODERATOR;
			} else if (this.roleTemp.toLowerCase().equals("donor")) {
				this.userRole = Role.DONOR;
			} else if (this.roleTemp.toLowerCase().equals("developer")) {
				this.userRole = Role.DEVELOPER;
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

	public void setClouds(final ArrayList<Cloud> clouds) {
		new Thread() {
			public void run() {
				for (Cloud c : clouds) {
					User.this.clouds.put(c.getId(), c);
				}
			};
		}.start();
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
		this.memberSince = convertDateString(iso8601);
	}

	public void setConfirmedRegistrationAtTemp(String iso8601) {
		this.confirmedRegistrationAtTemp = iso8601;
		this.confirmedRegistrationAt = convertDateString(iso8601);
	}

	public void setConfirmedRegistrationAt(Calendar confirmedRegistrationAt) {
		this.confirmedRegistrationAt = confirmedRegistrationAt;
	}

	public void setTncLastAcceptedTemp(String iso8601) {
		this.tncLastAcceptedTemp = iso8601;
		this.tncLastAccepted = convertDateString(iso8601);
	}

	public void setTncLastAccpeted(Calendar tncLastAccepted) {
		this.tncLastAccepted = tncLastAccepted;
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
		if (roleTemp.toLowerCase().equals("founder")) {
			this.userRole = Role.FOUNDER;
		} else if (roleTemp.toLowerCase().equals("admin")) {
			this.userRole = Role.ADMIN;
		} else if (roleTemp.toLowerCase().equals("moderator")) {
			this.userRole = Role.MODERATOR;
		} else if (roleTemp.toLowerCase().equals("donor")) {
			this.userRole = Role.DONOR;
		} else if (roleTemp.toLowerCase().equals("developer")) {
			this.userRole = Role.DEVELOPER;
		} else {
			this.userRole = Role.NORMAL;
		}
	}

	public void setSuspendedUntil(Calendar suspendedUntil) {
		this.suspendedUntil = suspendedUntil;
	}

	public void setSuspendedUntilTemp(String iso8601) {
		this.suspendedUntil = convertDateString(iso8601);
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

	public void addCloud(Cloud cloud) {
		clouds.put(cloud.getId(), cloud);
	}

	public Cloud getCloud(String id) {
		return clouds.get(id);
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