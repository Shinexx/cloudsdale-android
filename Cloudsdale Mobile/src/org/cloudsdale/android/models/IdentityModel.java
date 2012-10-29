package org.cloudsdale.android.models;

import com.google.gson.annotations.Expose;

/**
 * Model class to pass on identity properties to child models
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class IdentityModel extends Model {
	
	@Expose
	protected String	mId;

	public String getId() {
		return this.mId;
	}

	public void setId(String id) {
		this.mId = id;
	}

}
