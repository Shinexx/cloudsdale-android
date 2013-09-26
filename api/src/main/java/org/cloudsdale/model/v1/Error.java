package org.cloudsdale.model.v1;

import com.google.gson.annotations.SerializedName;

public class Error extends Model {
	
	private String	type;
	@SerializedName("ref_type")
	private String	refType;
	@SerializedName("ref_id")
	private String	refId;
	@SerializedName("ref_node")
	private String	refNode;
	private String	message;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getRefNode() {
        return refNode;
    }

    public void setRefNode(String refNode) {
        this.refNode = refNode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
