package org.cloudsdale.android.models.api;

import com.google.gson.annotations.SerializedName;

import org.cloudsdale.android.models.Model;

import lombok.Data;

@Data
public class Session extends Model {

	@SerializedName("client_id")
	private String	clientId;
	private User	user;

}
