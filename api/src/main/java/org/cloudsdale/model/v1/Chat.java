package org.cloudsdale.model.v1;

import java.util.List;

public class Chat extends Model {

    private Cloud				cloud;
    private List<Message>	messages;
    private String				token;

    public Cloud getCloud() {
        return cloud;
    }

    public void setCloud(Cloud cloud) {
        this.cloud = cloud;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
