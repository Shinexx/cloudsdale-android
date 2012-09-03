package org.cloudsdale.android.models;

import com.b3rwynmobile.fayeclient.models.FayeAdvice;
import com.google.gson.annotations.SerializedName;

import org.cloudsdale.android.models.api_models.Message;

public class CloudsdaleFayeMessage {

    // General
    private String     channel;

    // Handshake
    private String     version;
    private String[]   supportedConnectionTypes;
    private FayeAdvice advice;

    // Event
    private Message    data;

    // (Un-)Subscribe
    private String     subscription;
    private String     error;

    // Handshake + Subscribe
    private boolean    successful;
    @SerializedName("clientId")
    private String     clientId;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String[] getSupportedConnectionTypes() {
        return supportedConnectionTypes;
    }

    public void setSupportedConnectionTypes(String[] supportedConnectionTypes) {
        this.supportedConnectionTypes = supportedConnectionTypes;
    }

    public FayeAdvice getAdvice() {
        return advice;
    }

    public void setAdvice(FayeAdvice advice) {
        this.advice = advice;
    }

    public Message getData() {
        return data;
    }

    public void setData(Message data) {
        this.data = data;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
