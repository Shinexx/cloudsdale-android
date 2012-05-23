package org.cloudsdale.android.models;

import org.cloudsdale.android.annotations.GsonIgnore;

/**
 * Response bean for easy GSON (de)serialization
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class Response {
	// Data objects
	protected String	status;
	@GsonIgnore
	protected Error[]	errors;
	protected Flash		flash;

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the errors
	 */
	public Error[] getErrors() {
		return errors;
	}

	/**
	 * @param error
	 *            the errors to set
	 */
	public void setErrors(Error[] errors) {
		this.errors = errors;
	}

	/**
	 * @return the flash
	 */
	public Flash getFlash() {
		return flash;
	}

	/**
	 * @param the
	 *            flash to set
	 */
	public void setFlash(Flash flash) {
		this.flash = flash;
	}

}
