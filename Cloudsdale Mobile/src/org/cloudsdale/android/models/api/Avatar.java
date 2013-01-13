package org.cloudsdale.android.models.api;

import org.cloudsdale.android.models.Model;

import lombok.Data;

@Data
public class Avatar extends Model {

	private String	normal;
	private String	mini;
	private String	thumb;
	private String	preview;
	private String	chat;
	
}
