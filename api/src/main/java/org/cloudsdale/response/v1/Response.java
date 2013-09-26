package org.cloudsdale.response.v1;

import org.cloudsdale.model.v1.Flash;

/**
 * Response bean for easy GSON (de)serialization
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public abstract class Response {
	
	protected int		status;
	protected Error[]	errors;
	protected Flash flash;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Error[] getErrors() {
        return errors;
    }

    public void setErrors(Error[] errors) {
        this.errors = errors;
    }

    public Flash getFlash() {
        return flash;
    }

    public void setFlash(Flash flash) {
        this.flash = flash;
    }
}
