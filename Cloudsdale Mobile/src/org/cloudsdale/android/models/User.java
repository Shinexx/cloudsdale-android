package org.cloudsdale.android.models;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import com.bugsense.trace.BugSenseHandler;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * User model for Cloudsdale
 * 
 * @author Jamison Greeley (Berwyn@cloudsdale.org)
 */
public class User {

	private final String		TAG	= "Cloudsdale User";

	// Private attributes from JSON
	private String				name;
	@SerializedName("time_zone")
	private String				timeZone;
	@SuppressWarnings("unused")
	@SerializedName("member_since")
	private String				memberSinceTemp;
	private Calendar			memberSince;
	@SuppressWarnings("unused")
	@SerializedName("suspended_until")
	private String				suspendedUntilTemp;
	private Calendar			suspendedUntil;
	@SerializedName("reason_for_suspension")
	private String				reasonForSuspension;
	private String				id;
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
	@SuppressWarnings("unused")
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
	 * @see org.cloudsdale.android.models.User#toJson()
	 */
	public String toString() {
		return this.toJson();
	}

	public String getTAG() {
		return TAG;
	}

	public String getName() {
		return name;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public String getReasonForSuspension() {
		return reasonForSuspension;
	}

	public String getId() {
		return id;
	}

	public AvatarContainer getAvatar() {
		return avatar;
	}

	public boolean isRegistered() {
		return isRegistered;
	}

	public boolean isTransientStatus() {
		return transientStatus;
	}

	public boolean isBanStatus() {
		return banStatus;
	}

	public boolean isMemberOfACloud() {
		return memberOfACloud;
	}

	public boolean isHasAvatar() {
		return hasAvatar;
	}

	public boolean isHasReadTnC() {
		return hasReadTnC;
	}

	public Role getRole() {
		return userRole;
	}

	public Prosecution[] getProsecutions() {
		return prosecutions;
	}

	public String getAuthToken() {
		return authToken;
	}

	public String getEmail() {
		return email;
	}

	public boolean isNeedsToConfirmRegistration() {
		return needsToConfirmRegistration;
	}

	public boolean isNeedsToChangePassword() {
		return needsToChangePassword;
	}

	public boolean isNeedsToChangeName() {
		return needsToChangeName;
	}

	public ArrayList<Cloud> getClouds() {
		return clouds;
	}

	public Calendar getMemberSince() {
		return memberSince;
	}

	public Calendar getSuspendedUntil() {
		return suspendedUntil;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public void setMemberSince(Calendar memberSince) {
		this.memberSince = memberSince;
	}

	public void setMemberSinceTemp(String iso8601) {
		try {
			memberSince = ISO8601.toCalendar(iso8601);
		} catch (ParseException e) {
			BugSenseHandler.log(TAG, e);
		}
	}

	public void setSuspendedUntil(Calendar suspendedUntil) {
		this.suspendedUntil = suspendedUntil;
	}

	public void setSuspendedUntilTemp(String iso8601) {
		try {
			suspendedUntil = ISO8601.toCalendar(iso8601);
		} catch (ParseException e) {
			BugSenseHandler.log(TAG, e);
		}
	}

	public void setReasonForSuspension(String reasonForSuspension) {
		this.reasonForSuspension = reasonForSuspension;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setAvatar(AvatarContainer avatar) {
		this.avatar = avatar;
	}

	public void setRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

	public void setTransientStatus(boolean transientStatus) {
		this.transientStatus = transientStatus;
	}

	public void setBanStatus(boolean banStatus) {
		this.banStatus = banStatus;
	}

	public void setMemberOfACloud(boolean memberOfACloud) {
		this.memberOfACloud = memberOfACloud;
	}

	public void setHasAvatar(boolean hasAvatar) {
		this.hasAvatar = hasAvatar;
	}

	public void setHasReadTnC(boolean hasReadTnC) {
		this.hasReadTnC = hasReadTnC;
	}

	public void setRoleTemp(String roleTemp) {
		if (roleTemp.toLowerCase().equals("creator")) {
			userRole = Role.CREATOR;
		} else if (roleTemp.toLowerCase().equals("admin")) {
			userRole = Role.ADMIN;
		} else if (roleTemp.toLowerCase().equals("moderator")) {
			userRole = Role.MODERATOR;
		} else if (roleTemp.toLowerCase().equals("donor")) {
			userRole = Role.DONOR;
		} else {
			userRole = Role.NORMAL;
		}
	}

	public void setProsecutions(Prosecution[] prosecutions) {
		this.prosecutions = prosecutions;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setNeedsToConfirmRegistration(boolean needsToConfirmRegistration) {
		this.needsToConfirmRegistration = needsToConfirmRegistration;
	}

	public void setNeedsToChangePassword(boolean needsToChangePassword) {
		this.needsToChangePassword = needsToChangePassword;
	}

	public void setNeedsToChangeName(boolean needsToChangeName) {
		this.needsToChangeName = needsToChangeName;
	}

	public void setClouds(ArrayList<Cloud> clouds) {
		this.clouds = clouds;
	}

}