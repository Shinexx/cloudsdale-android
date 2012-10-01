package org.cloudsdale.android.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "avatars")
public class AvatarContainer extends Model {

	// Private attributes
    @DatabaseField
	private String	normal;
    @DatabaseField
	private String	mini;
    @DatabaseField
	private String	thumb;
    @DatabaseField
	private String	preview;
    @DatabaseField
	private String	chat;

	/**
	 * @return the chat
	 */
	public String getChat() {
		return this.chat;
	}

	/**
	 * @return the mini
	 */
	public String getMini() {
		return this.mini;
	}

	/**
	 * @return the normal
	 */
	public String getNormal() {
		return this.normal;
	}

	/**
	 * @return the preview
	 */
	public String getPreview() {
		return this.preview;
	}

	/**
	 * @return the thumb
	 */
	public String getThumb() {
		return this.thumb;
	}

	/**
	 * @param chat
	 *            the chat to set
	 */
	public void setChat(String chat) {
		this.chat = chat;
	}

	/**
	 * @param mini
	 *            the mini to set
	 */
	public void setMini(String mini) {
		this.mini = mini;
	}

	/**
	 * @param normal
	 *            the normal to set
	 */
	public void setNormal(String normal) {
		this.normal = normal;
	}

	/**
	 * @param preview
	 *            the preview to set
	 */
	public void setPreview(String preview) {
		this.preview = preview;
	}

	/**
	 * @param thumb
	 *            the thumb to set
	 */
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

}
