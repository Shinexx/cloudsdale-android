package org.cloudsdale.android.models.api;

import org.cloudsdale.android.models.Model;

public class Flash extends Model {
	private String	message;
	private String	type;
	private String	title;

	/**
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
