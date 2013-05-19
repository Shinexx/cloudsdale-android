package org.cloudsdale.android.models.network;

import org.cloudsdale.android.models.api.Session;

public class SessionResponse extends Response {
	
	private Session result;

    public Session getResult() {
        return result;
    }

    public void setResult(Session result) {
        this.result = result;
    }
}
