package org.cloudsdale.android.models.api;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.cloudsdale.android.models.Model;

import com.google.gson.annotations.SerializedName;

@Data
@EqualsAndHashCode(callSuper=false)
public class Message extends Model {

	private Date		timestamp;
	private String		content;
	private User		author;
	private String[]	urls;
	private String		device;
	@SerializedName("author_id")
	private String		authorId;
	private Drop[]		drops;

}
