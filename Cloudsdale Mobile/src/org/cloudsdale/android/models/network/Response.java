package org.cloudsdale.android.models.network;

import org.cloudsdale.android.models.api.Error;
import org.cloudsdale.android.models.api.Flash;

import lombok.Data;

/**
 * Response bean for easy GSON (de)serialization
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
@Data
public class Response {
	
	protected int		status;
	protected Error[]	errors;
	protected Flash		flash;

}
