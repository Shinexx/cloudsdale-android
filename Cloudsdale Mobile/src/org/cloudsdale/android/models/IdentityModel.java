package org.cloudsdale.android.models;

import com.j256.ormlite.field.DatabaseField;

/**
 * Model class to pass on identity properties to child models
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class IdentityModel extends Model {

    @DatabaseField(id = true)
	protected String	id;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
