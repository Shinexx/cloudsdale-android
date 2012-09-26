package org.cloudsdale.android.models.network;

public class FacebookResponse extends Response {
	private String	id;
	private String	type;

	public FacebookResponse() {
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

}
