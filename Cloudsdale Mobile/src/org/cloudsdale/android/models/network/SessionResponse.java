package org.cloudsdale.android.models.network;

import org.cloudsdale.android.models.api.Session;

import lombok.Data;

@Data
public class SessionResponse extends Response {
	
	private Session result;

}
