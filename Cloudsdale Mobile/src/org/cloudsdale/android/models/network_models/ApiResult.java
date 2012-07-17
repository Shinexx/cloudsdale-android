package org.cloudsdale.android.models.network_models;

import org.cloudsdale.android.models.IdentityModel;

public class ApiResult extends Result {

	protected IdentityModel	model;

	public IdentityModel getModel() {
		return this.model;
	}

	public void setModel(IdentityModel model) {
		this.model = model;
	}

}
