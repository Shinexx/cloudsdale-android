package org.cloudsdale.android.models.api_models;

import org.cloudsdale.android.models.Model;

public class Chat extends Model {

	// Relationships
	private Cloud	cloud;

	/**
	 * Default constructor
	 */
	public Chat() {
		this.cloud = null;
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

	public Cloud getCloud() {
		return this.cloud;
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
