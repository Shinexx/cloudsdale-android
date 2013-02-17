package org.cloudsdale.android.models.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.cloudsdale.android.models.Model;

@Data
@EqualsAndHashCode(callSuper=false)
public class Flash extends Model {
	
	private String	message;
	private String	type;
	private String	title;
	
}
