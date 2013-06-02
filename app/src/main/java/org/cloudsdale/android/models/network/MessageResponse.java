package org.cloudsdale.android.models.network;

import org.cloudsdale.android.models.api.Message;

import java.util.ArrayList;

public class MessageResponse extends Response {

	private Message result;
	private ArrayList<Message> results;

    public Message getResult() {
        return result;
    }

    public void setResult(Message result) {
        this.result = result;
    }

    public ArrayList<Message> getResults() {
        return results;
    }

    public void setResults(ArrayList<Message> results) {
        this.results = results;
    }
}
