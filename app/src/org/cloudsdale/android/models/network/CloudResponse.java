package org.cloudsdale.android.models.network;

import org.cloudsdale.android.models.api.Cloud;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CloudResponse extends Response {

	private Cloud result;
	private ArrayList<Cloud> results;
	
}
