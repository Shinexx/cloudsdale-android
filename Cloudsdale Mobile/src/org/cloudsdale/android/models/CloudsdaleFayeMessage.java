package org.cloudsdale.android.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.cloudsdale.android.models.api.Message;

import com.b3rwynmobile.fayeclient.models.FayeAdvice;

@Data
@EqualsAndHashCode(callSuper=false)
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
    private String     clientId;

}
