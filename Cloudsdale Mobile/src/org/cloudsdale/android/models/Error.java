package org.cloudsdale.android.models;

import com.google.gson.annotations.SerializedName;

public class Error {
	private String	type;
	@SerializedName("ref_type")
	private String	refType;
	@SerializedName("ref_id")
	private String	refId;
	@SerializedName("ref_node")
	private String	refNode;
	private String	message;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the refType
	 */
	public String getRefType() {
		return refType;
	}

	/**
	 * @param refType
	 *            the refType to set
	 */
	public void setRefType(String refType) {
		this.refType = refType;
	}

	/**
	 * @return the refId
	 */
	public String getRefId() {
		return refId;
	}

	/**
	 * @param refId
	 *            the refId to set
	 */
	public void setRefId(String refId) {
		this.refId = refId;
	}

	/**
	 * @return the refNode
	 */
	public String getRefNode() {
		return refNode;
	}

	/**
	 * @param refNode
	 *            the refNode to set
	 */
	public void setRefNode(String refNode) {
		this.refNode = refNode;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
