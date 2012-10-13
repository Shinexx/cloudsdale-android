package org.cloudsdale.android.models;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Model class to pass on identity properties to child models
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class IdentityModel<T> extends Model<T> {
	
	public IdentityModel(Context context) {
		super(context);
	}

	@Expose
	@SerializedName("id")
	protected String	stringId;

	public String getStringId() {
		return this.stringId;
	}

	public void setStringId(String id) {
		this.stringId = id;
	}

}
