package org.cloudsdale.android.models.network;

import org.cloudsdale.android.models.api.Session;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionResponse extends Response {
	
	private Session result;

}
