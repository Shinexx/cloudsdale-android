package org.cloudsdale.model.v1;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * User model for Cloudsdale
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
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

	protected final String	TAG	= "Cloudsdale User";

	// protected attributes from JSON
	protected String		name;
	protected String		username;
	protected String		email;
	@SerializedName("skype_name")
	protected String		skypeName;
	protected boolean		invisible;
	@SerializedName("time_zone")
	protected String		timeZone;
	@SerializedName("member_since")
	protected Date			memberSince;
	@SerializedName("suspended_until")
	protected Date			suspendedUntil;
	@SerializedName("reason_for_suspension")
	protected String		reasonForSuspension;
	protected Avatar		avatar;
	@SerializedName("is_registered")
	protected boolean		isRegistered;
	@SerializedName("is_transient")
	protected boolean		transientStatus;
	@SerializedName("is_banned")
	protected boolean		banStatus;
	@SerializedName("is_member_of_a_cloud")
	protected boolean		memberOfACloud;
	@SerializedName("has_an_avatar")
	protected boolean		hasAvatar;
	@SerializedName("has_read_tnc")
	protected boolean		hasReadTnC;
	@SerializedName("role")
	protected Role			userRole;
	protected Prosecution[]	prosecutions;
	@SerializedName("auth_token")
	protected String		authToken;
	@SerializedName("needs_to_confirm_registration")
	protected boolean		needsToConfirmRegistration;
	@SerializedName("needs_password_change")
	protected boolean		needsToChangePassword;
	@SerializedName("needs_name_change")
	protected boolean		needsToChangeName;
	@SerializedName("confirmed_registration_at")
	protected Date			confirmedRegistrationAt;
	@SerializedName("tnc_last_accepted")
	protected Date			tncLastAccepted;
	protected List<Cloud>	clouds;
	protected Ban[]			bans;

	public void setUserRole(Role role) {
		this.userRole = role;
	}

	public void addCloud(Cloud cloud) {
		clouds.add(cloud);
	}

	public void setClouds(List<Cloud> clouds) {
		this.clouds = clouds;
	}

	public String getTAG() {
		return TAG;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSkypeName() {
		return skypeName;
	}

	public void setSkypeName(String skypeName) {
		this.skypeName = skypeName;
	}

	public boolean isInvisible() {
		return invisible;
	}

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public Date getMemberSince() {
		return memberSince;
	}

	public void setMemberSince(Date memberSince) {
		this.memberSince = memberSince;
	}

	public Date getSuspendedUntil() {
		return suspendedUntil;
	}

	public void setSuspendedUntil(Date suspendedUntil) {
		this.suspendedUntil = suspendedUntil;
	}

	public String getReasonForSuspension() {
		return reasonForSuspension;
	}

	public void setReasonForSuspension(String reasonForSuspension) {
		this.reasonForSuspension = reasonForSuspension;
	}

	public Avatar getAvatar() {
		return avatar;
	}

	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}

	public boolean isRegistered() {
		return isRegistered;
	}

	public void setRegistered(boolean registered) {
		isRegistered = registered;
	}

	public boolean isTransientStatus() {
		return transientStatus;
	}

	public void setTransientStatus(boolean transientStatus) {
		this.transientStatus = transientStatus;
	}

	public boolean isBanStatus() {
		return banStatus;
	}

	public void setBanStatus(boolean banStatus) {
		this.banStatus = banStatus;
	}

	public boolean isMemberOfACloud() {
		return memberOfACloud;
	}

	public void setMemberOfACloud(boolean memberOfACloud) {
		this.memberOfACloud = memberOfACloud;
	}

	public boolean isHasAvatar() {
		return hasAvatar;
	}

	public void setHasAvatar(boolean hasAvatar) {
		this.hasAvatar = hasAvatar;
	}

	public boolean isHasReadTnC() {
		return hasReadTnC;
	}

	public void setHasReadTnC(boolean hasReadTnC) {
		this.hasReadTnC = hasReadTnC;
	}

	public Role getUserRole() {
		return userRole;
	}

	public Prosecution[] getProsecutions() {
		return prosecutions;
	}

	public void setProsecutions(Prosecution[] prosecutions) {
		this.prosecutions = prosecutions;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public boolean isNeedsToConfirmRegistration() {
		return needsToConfirmRegistration;
	}

	public void setNeedsToConfirmRegistration(boolean needsToConfirmRegistration) {
		this.needsToConfirmRegistration = needsToConfirmRegistration;
	}

	public boolean isNeedsToChangePassword() {
		return needsToChangePassword;
	}

	public void setNeedsToChangePassword(boolean needsToChangePassword) {
		this.needsToChangePassword = needsToChangePassword;
	}

	public boolean isNeedsToChangeName() {
		return needsToChangeName;
	}

	public void setNeedsToChangeName(boolean needsToChangeName) {
		this.needsToChangeName = needsToChangeName;
	}

	public Date getConfirmedRegistrationAt() {
		return confirmedRegistrationAt;
	}

	public void setConfirmedRegistrationAt(Date confirmedRegistrationAt) {
		this.confirmedRegistrationAt = confirmedRegistrationAt;
	}

	public Date getTncLastAccepted() {
		return tncLastAccepted;
	}

	public void setTncLastAccepted(Date tncLastAccepted) {
		this.tncLastAccepted = tncLastAccepted;
	}

	public List<Cloud> getClouds() {
		return clouds;
	}

	public Ban[] getBans() {
		return bans;
	}

	public void setBans(Ban[] bans) {
		this.bans = bans;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
