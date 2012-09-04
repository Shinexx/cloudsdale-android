package org.cloudsdale.android.models.network_models;

import org.cloudsdale.android.models.api_models.Message;

public class ApiMessageResponse extends ApiResponse {

    private Message result;
    
    public Message getResult() {
        return result;
    }
    
    public void setResult(Message result) {
        this.result = result;
    }
    
}
