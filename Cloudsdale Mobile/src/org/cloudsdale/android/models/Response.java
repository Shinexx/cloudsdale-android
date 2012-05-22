package org.cloudsdale.android.models;


/**
 * Response bean for easy GSON (de)serialization
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class Response {
	// Data objects
	private String		status;
	private String[]	errors;
	private String		flash;
	private String		result;

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
	public String[] getErrors() {
		return errors;
	}

	/**
	 * @param error
	 *            the errors to set
	 */
	public void setErrors(String[] errors) {
		this.errors = errors;
	}

	/**
	 * @return the flash
	 */
	public String getFlash() {
		return flash;
	}

	/**
	 * @param the
	 *            flash to set
	 */
	public void setFlash(String flash) {
		this.flash = flash;
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
}
