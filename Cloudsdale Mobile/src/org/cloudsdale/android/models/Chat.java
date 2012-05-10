package org.cloudsdale.android.models;

public class Chat {

	// Relationships
	private Cloud cloud;

	/**
	 * Default constructor
	 */
	public Chat() {
		cloud = null;
	}

	/**
	 * Parameterized constructor
	 * 
	 * @param cloud
	 *            cloud that this chat belongs to
	 */
	public Chat(Cloud cloud) {
		this.cloud = cloud;
	}

	/**
	 * Set the cloud that this chat belongs to
	 * 
	 * @param cloud
	 *            the cloud that this chat belongs to
	 */
	public void setCloud(Cloud cloud) {
		this.cloud = cloud;
	}

}
