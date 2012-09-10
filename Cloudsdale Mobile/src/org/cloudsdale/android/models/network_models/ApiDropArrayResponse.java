package org.cloudsdale.android.models.network_models;

import org.cloudsdale.android.models.api_models.Drop;

public class ApiDropArrayResponse extends ApiResponse {
    
    private Drop[] result;
    
    public Drop[] getResult() {
        return result;
    }
    
    public void setResult(Drop[] result) {
        this.result = result;
    }

}
