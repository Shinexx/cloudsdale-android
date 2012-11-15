package org.cloudsdale.android.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AvatarContainer {
	
	private String	normal;
	private String	mini;
	private String	thumb;
	private String	preview;
	private String	chat;

}
