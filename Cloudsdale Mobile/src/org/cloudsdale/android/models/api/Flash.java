package org.cloudsdale.android.models.api;

import android.content.Context;

import com.google.gson.annotations.Expose;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.models.Model;

public class Flash extends Model<Flash> {
	
	@Expose
	private String	message;
	@Expose
	private String	type;
	@Expose
	private String	title;
	
	public Flash() {
		this(Cloudsdale.getContext());
	}
	
	public Flash(Context context) {
		super(context);
	}

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
