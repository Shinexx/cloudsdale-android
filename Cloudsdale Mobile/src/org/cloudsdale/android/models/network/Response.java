package org.cloudsdale.android.models.network;

import com.google.gson.annotations.Expose;

import org.cloudsdale.android.models.api.Error;
import org.cloudsdale.android.models.api.Flash;

/**
 * Response bean for easy GSON (de)serialization
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class Response {
	
	// Data objects
	@Expose
	protected int		status;
	@Expose
	protected Error[]	errors;
	@Expose
	protected Flash		flash;

	/**
	 * @return the errors
	 */
	public Error[] getErrors() {
		return this.errors;
	}

	/**
	 * @return the flash
	 */
	public Flash getFlash() {
		return this.flash;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * @param error
	 *            the errors to set
	 */
	public void setErrors(Error[] errors) {
		this.errors = errors;
	}

	/**
	 * @param the
	 *            flash to set
	 */
	public void setFlash(Flash flash) {
		this.flash = flash;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

}
