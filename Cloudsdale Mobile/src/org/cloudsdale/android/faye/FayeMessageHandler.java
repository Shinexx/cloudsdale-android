package org.cloudsdale.android.faye;

import org.cloudsdale.android.models.CloudsdaleFayeMessage;


public interface FayeMessageHandler {
    
    public void handleMessage(CloudsdaleFayeMessage msg);

}
