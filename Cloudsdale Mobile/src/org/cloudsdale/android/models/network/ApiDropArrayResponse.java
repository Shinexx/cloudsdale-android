package org.cloudsdale.android.models.network;

import org.cloudsdale.android.models.api.Drop;

public class ApiDropArrayResponse extends ApiResponse {
    
    private Drop[] result;
    
    public Drop[] getResult() {
        return result;
    }
    
    public void setResult(Drop[] result) {
        this.result = result;
    }

}
