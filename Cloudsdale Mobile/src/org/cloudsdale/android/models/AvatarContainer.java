package org.cloudsdale.android.models;

import android.content.Context;

import com.j256.ormlite.table.DatabaseTable;

import org.cloudsdale.android.Cloudsdale;

import java.io.Serializable;

@DatabaseTable(tableName = "avatars")
public class AvatarContainer extends Model<AvatarContainer> implements Serializable {
	
	public AvatarContainer() {
		this(Cloudsdale.getContext());
	}

	public AvatarContainer(Context context) {
		super(context);
	}

	/**
	 * Generated SUID
	 */
	private static final long	serialVersionUID	= 1359536748460453528L;
	
	// Private attributes
	private String	normal;
	private String	mini;
	private String	thumb;
	private String	preview;
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
