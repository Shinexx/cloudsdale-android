package org.cloudsdale.android.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class QueryData {

	private String						url;
	private String						json;

}