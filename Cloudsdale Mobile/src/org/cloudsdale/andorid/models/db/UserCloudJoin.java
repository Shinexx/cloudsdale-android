package org.cloudsdale.andorid.models.db;

import android.content.Context;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.models.Model;
import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.api.User;

public class UserCloudJoin extends Model {
	
	private User mUser;
	private Cloud mCloud;
	
	public UserCloudJoin() {
		this(null, null);
	}
	
	public UserCloudJoin(User user, Cloud cloud) {
		this(user, cloud, Cloudsdale.getContext());
	}
	
	public UserCloudJoin(User user, Cloud cloud, Context context) {
		this.mUser = user;
		this.mCloud = cloud;
	}
	
	public User getUser() {
		return mUser;
	}
	
	public Cloud getCloud() {
		return mCloud;
	}
	
	public void setUser(User user) {
		this.mUser = user;
	}
	
	public void setCloud(Cloud cloud) {
		this.mCloud = cloud;
	}

}
