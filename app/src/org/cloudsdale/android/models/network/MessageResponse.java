package org.cloudsdale.android.models.network;

import org.cloudsdale.android.models.api.Message;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse extends Response {

	private Message result;
	private ArrayList<Message> results;
	
}
