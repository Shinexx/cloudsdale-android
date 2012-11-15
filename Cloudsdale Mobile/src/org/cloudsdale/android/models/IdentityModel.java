package org.cloudsdale.android.models;

import lombok.Data;
import lombok.EqualsAndHashCode;



/**
 * Model class to pass on identity properties to child models
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class IdentityModel extends Model {

	protected String	id;

}
