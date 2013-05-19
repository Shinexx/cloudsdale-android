package org.cloudsdale.android.models.api;

import java.util.ArrayList;

import org.cloudsdale.android.models.Model;

public class Chat extends Model {

    private Cloud				cloud;
    private ArrayList<Message>	messages;
    private String				token;

    public Cloud getCloud() {
        return cloud;
    }

    public void setCloud(Cloud cloud) {
        this.cloud = cloud;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
