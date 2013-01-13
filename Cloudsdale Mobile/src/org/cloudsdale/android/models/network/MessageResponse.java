package org.cloudsdale.android.models.network;

import org.cloudsdale.android.models.api.Message;

import java.util.ArrayList;

import lombok.Data;

@Data
public class MessageResponse extends Response {

	private Message result;
	private ArrayList<Message> results;
	
}
