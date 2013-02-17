package org.cloudsdale.android.models.network;

import org.cloudsdale.android.models.api.Cloud;

import java.util.ArrayList;

import lombok.Data;

@Data
public class CloudResponse extends Response {

	private Cloud result;
	private ArrayList<Cloud> results;
	
}
