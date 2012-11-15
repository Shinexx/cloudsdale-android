package org.cloudsdale.android.models.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.cloudsdale.android.models.Model;

import com.google.gson.annotations.SerializedName;

@Data
@EqualsAndHashCode(callSuper=false)
public class Error extends Model {
	
	private String	type;
	@SerializedName("ref_type")
	private String	refType;
	@SerializedName("ref_id")
	private String	refId;
	@SerializedName("ref_node")
	private String	refNode;
	private String	message;
	
}
