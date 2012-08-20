package org.cloudsdale.android.faye;

import com.b3rwynmobile.fayeclient.models.FayeMessage;

public interface FayeMessageHandler {
    
    public void handleMessage(FayeMessage message);

}
