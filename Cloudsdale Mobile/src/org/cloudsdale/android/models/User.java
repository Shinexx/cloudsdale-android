package org.cloudsdale.android.models;

import java.text.ParseException;
import java.util.Calendar;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * User model for Cloudsdale
 * 
 * @author Jamison Greeley (Berwyn@cloudsdale.org)
 */
public class User {

	private final String	TAG	= "Cloudsdale User";

	// Private attributes
	private String			name;
	@SerializedName("time_zone")
	private String			timeZone;
	@SerializedName("member_since")
	private String			memberSinceTemp;
	@SerializedName("suspended_until")
	private String			suspendedUntilTemp;
	private Calendar		memberSince;
	private Calendar		suspendedUntil;
	@SerializedName("reason_for_suspension")
	private String			reasonForSuspension;
	private String			id;
	private AvatarContainer	avatar;
	@SerializedName("is_registered")
	private boolean			isRegistered;
	@SerializedName("is_transient")
	private boolean			isTransient;
	@SerializedName("is_banned")
	private boolean			isBanned;
	private Prosecution[]	prosecutions;
	@SerializedName("auth_token")
	private String			authToken;
	private String			email;
	@SerializedName("needs_to_confirm_registration")
	private boolean			needsToConfirmRegistration;
	@SerializedName("needs_password_change")
	private boolean			needsToChangePassword;
	@SerializedName("needs_name_change")
	private boolean			needsToChangeName;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * @param timeZone
	 *            the timeZone to set
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * @return the memberSinceTemp
	 */
	public String getMemberSinceTemp() {
		return memberSinceTemp;
	}

	/**
	 * @param memberSinceTemp
	 *            the memberSinceTemp to set
	 */
	public void setMemberSinceTemp(String memberSinceTemp) {
		try {
			Calendar cal = ISO8601.toCalendar(memberSinceTemp);
			setMemberSince(cal);
		} catch (ParseException e) {
			Log.e(TAG, "Parse Exception: " + e.getMessage());
		}
		this.memberSinceTemp = memberSinceTemp;
	}

	/**
	 * @return the suspendedUntilTemp
	 */
	public String getSuspendedUntilTemp() {
		return suspendedUntilTemp;
	}

	/**
	 * @param suspendedUntilTemp
	 *            the suspendedUntilTemp to set
	 */
	public void setSuspendedUntilTemp(String suspendedUntilTemp) {
		try {
			Calendar cal = ISO8601.toCalendar(memberSinceTemp);
			setSuspendedUntil(cal);
		} catch (ParseException e) {
			Log.e(TAG, "Parse Exception: " + e.getMessage());
		}
		this.suspendedUntilTemp = suspendedUntilTemp;
	}

	/**
	 * @return the memberSince
	 */
	public Calendar getMemberSince() {
		return memberSince;
	}

	/**
	 * @param memberSince
	 *            the memberSince to set
	 */
	public void setMemberSince(Calendar memberSince) {
		this.memberSince = memberSince;
	}

	/**
	 * @return the suspendedUntil
	 */
	public Calendar getSuspendedUntil() {
		return suspendedUntil;
	}

	/**
	 * @param suspendedUntil
	 *            the suspendedUntil to set
	 */
	public void setSuspendedUntil(Calendar suspendedUntil) {
		this.suspendedUntil = suspendedUntil;
	}

	/**
	 * @return the reasonForSuspension
	 */
	public String getReasonForSuspension() {
		return reasonForSuspension;
	}

	/**
	 * @param reasonForSuspension
	 *            the reasonForSuspension to set
	 */
	public void setReasonForSuspension(String reasonForSuspension) {
		this.reasonForSuspension = reasonForSuspension;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the avatar
	 */
	public AvatarContainer getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar
	 *            the avatar to set
	 */
	public void setAvatar(AvatarContainer avatar) {
		this.avatar = avatar;
	}

	/**
	 * @return the isRegistered
	 */
	public boolean isRegistered() {
		return isRegistered;
	}

	/**
	 * @param isRegistered
	 *            the isRegistered to set
	 */
	public void setRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

	/**
	 * @return the isTransient
	 */
	public boolean isTransient() {
		return isTransient;
	}

	/**
	 * @param isTransient
	 *            the isTransient to set
	 */
	public void setTransient(boolean isTransient) {
		this.isTransient = isTransient;
	}

	/**
	 * @return the isBanned
	 */
	public boolean isBanned() {
		return isBanned;
	}

	/**
	 * @param isBanned
	 *            the isBanned to set
	 */
	public void setBanned(boolean isBanned) {
		this.isBanned = isBanned;
	}

	/**
	 * @return the prosecutions
	 */
	public Prosecution[] getProsecutions() {
		return prosecutions;
	}

	/**
	 * @param prosecutions
	 *            the prosecutions to set
	 */
	public void setProsecutions(Prosecution[] prosecutions) {
		this.prosecutions = prosecutions;
	}

	/**
	 * @return the authToken
	 */
	public String getAuthToken() {
		return authToken;
	}

	/**
	 * @param authToken
	 *            the authToken to set
	 */
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the needsToConfirmRegistration
	 */
	public boolean isNeedsToConfirmRegistration() {
		return needsToConfirmRegistration;
	}

	/**
	 * @param needsToConfirmRegistration
	 *            the needsToConfirmRegistration to set
	 */
	public void setNeedsToConfirmRegistration(boolean needsToConfirmRegistration) {
		this.needsToConfirmRegistration = needsToConfirmRegistration;
	}

	/**
	 * @return the needsToChangePassword
	 */
	public boolean isNeedsToChangePassword() {
		return needsToChangePassword;
	}

	/**
	 * @param needsToChangePassword
	 *            the needsToChangePassword to set
	 */
	public void setNeedsToChangePassword(boolean needsToChangePassword) {
		this.needsToChangePassword = needsToChangePassword;
	}

	/**
	 * @return the needsToChangeName
	 */
	public boolean isNeedsToChangeName() {
		return needsToChangeName;
	}

	/**
	 * @param needsToChangeName
	 *            the needsToChangeName to set
	 */
	public void setNeedsToChangeName(boolean needsToChangeName) {
		this.needsToChangeName = needsToChangeName;
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
	 * @see org.cloudsdale.android.models.User#toJson()
	 */
	public String toString() {
		return this.toJson();
	}
}