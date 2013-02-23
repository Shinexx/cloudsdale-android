package org.cloudsdale.android.models.api;

import com.google.gson.annotations.SerializedName;

import org.cloudsdale.android.models.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Session extends Model {

	@SerializedName("client_id")
	private String	clientId;
	private User	user;

}
