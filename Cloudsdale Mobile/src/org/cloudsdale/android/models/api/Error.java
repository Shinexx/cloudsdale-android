package org.cloudsdale.android.models.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.cloudsdale.android.models.Model;

public class Error extends Model {
	
	@Expose
	private String	type;
	@Expose
	@SerializedName("ref_type")
	private String	refType;
	@Expose
	@SerializedName("ref_id")
	private String	refId;
	@Expose
	@SerializedName("ref_node")
	private String	refNode;
	@Expose
	private String	message;
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * @return the refId
	 */
	public String getRefId() {
		return this.refId;
	}

	/**
	 * @return the refNode
	 */
	public String getRefNode() {
		return this.refNode;
	}

	/**
	 * @return the refType
	 */
	public String getRefType() {
		return this.refType;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @param refId
	 *            the refId to set
	 */
	public void setRefId(String refId) {
		this.refId = refId;
	}

	/**
	 * @param refNode
	 *            the refNode to set
	 */
	public void setRefNode(String refNode) {
		this.refNode = refNode;
	}

	/**
	 * @param refType
	 *            the refType to set
	 */
	public void setRefType(String refType) {
		this.refType = refType;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
