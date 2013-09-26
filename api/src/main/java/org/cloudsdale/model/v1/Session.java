package org.cloudsdale.model.v1;

import com.google.gson.annotations.SerializedName;

public class Session extends Model {

	@SerializedName("client_id")
	private String	clientId;
	private User	user;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
