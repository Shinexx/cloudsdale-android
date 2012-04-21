package org.cloudsdale.android.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

/**
 * User model for Cloudsdale
 * 
 * @author Jamison Greeley (Berwyn@cloudsdale.org)
 */
public class User {

	// Private attributes
	private String email;
	private String authToken;
	private String timeZone; // DateTime? DateFormat?
	private int role;
	private Calendar memberSince;
	private boolean invisible;
	private boolean forcePasswordChange;
	private Calendar tncLastAccepted;
	private String id;
	private String name;
	private ArrayList<String> avatarUrls;

	// Relationships
	private ArrayList<Cloud> ownedClouds;
	private ArrayList<Cloud> clouds;

	/**
	 * Default constructor
	 */
	public User() {
		email = "";
		authToken = "";
		timeZone = "";
		role = 0;
		memberSince = Calendar.getInstance();
		invisible = false;
		forcePasswordChange = false;
		tncLastAccepted = null;

		ownedClouds = new ArrayList<Cloud>();
		clouds = new ArrayList<Cloud>();
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
	 * @return the role
	 */
	public int getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(int role) {
		this.role = role;
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
	public void setMemberSince(Date memberSince) {
		Calendar c = Calendar.getInstance();
		c.setTime(memberSince);
		this.memberSince = c;
	}

	/**
	 * @return the invisible
	 */
	public boolean isInvisible() {
		return invisible;
	}

	/**
	 * @param invisible
	 *            the invisible to set
	 */
	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	/**
	 * @return the forcePasswordChange
	 */
	public boolean isForcePasswordChange() {
		return forcePasswordChange;
	}

	/**
	 * @param forcePasswordChange
	 *            the forcePasswordChange to set
	 */
	public void setForcePasswordChange(boolean forcePasswordChange) {
		this.forcePasswordChange = forcePasswordChange;
	}

	/**
	 * @return the tncLastAccepted
	 */
	public Calendar getTncLastAccepted() {
		return tncLastAccepted;
	}

	/**
	 * @param tncLastAccepted
	 *            the tncLastAccepted to set
	 */
	public void setTncLastAccepted(Calendar tncLastAccepted) {
		this.tncLastAccepted = tncLastAccepted;
	}

	/**
	 * @return the ownedClouds
	 */
	public ArrayList<Cloud> getOwnedClouds() {
		return ownedClouds;
	}

	/**
	 * @param ownedClouds
	 *            the ownedClouds to set
	 */
	public void setOwnedClouds(ArrayList<Cloud> ownedClouds) {
		this.ownedClouds = ownedClouds;
	}

	/**
	 * @return the clouds
	 */
	public ArrayList<Cloud> getClouds() {
		return clouds;
	}

	/**
	 * @param clouds
	 *            the clouds to set
	 */
	public void setClouds(ArrayList<Cloud> clouds) {
		this.clouds = clouds;
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
	 * @return the avatarUrls
	 */
	public ArrayList<String> getAvatarUrls() {
		return avatarUrls;
	}

	/**
	 * @param avatarUrls
	 *            the avatarUrls to set
	 */
	public void setAvatarUrls(ArrayList<String> avatarUrls) {
		this.avatarUrls = avatarUrls;
	}

	/**
	 * Adds the cloud to the list of owned clouds
	 * 
	 * @param c
	 *            the cloud that the user owns
	 */
	public void addOwnedCloud(Cloud c) {
		ownedClouds.add(c);
	}

	/**
	 * Removes a cloud from a user's ownership
	 * 
	 * @param c
	 *            the cloud to remove from the user's ownership
	 */
	public void removeOwnedCloud(Cloud c) {
		if (ownedClouds.contains(c))
			ownedClouds.remove(c);
	}

	/**
	 * Add a cloud to the user's clouds
	 * 
	 * @param c
	 *            the cloud the user is joining
	 */
	public void joinCloud(Cloud c) {
		clouds.add(c);
	}

	/**
	 * Remove a cloud from the list of a user's clouds
	 * 
	 * @param c
	 *            the cloud the user is leaving
	 */
	public void leaveCloud(Cloud c) {
		if (clouds.contains(c))
			clouds.remove(c);
	}

	/**
	 * Determine whether or not a user is a member of a cloud
	 * 
	 * @param c
	 *            the cloud that a user is or is not a member of
	 * @return whether or not the user is a member of the cloud
	 */
	public boolean isMemberOfCloud(Cloud c) {
		return clouds.contains(c);
	}
	
	public String toJson() {
		return new Gson().toJson(this, this.getClass());
	}
}