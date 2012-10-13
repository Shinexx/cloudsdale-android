package org.cloudsdale.android.models.network;

import com.google.gson.annotations.Expose;

import org.cloudsdale.android.models.api.Drop;

public class ApiDropArrayResponse extends ApiResponse {
    
	@Expose
    private Drop[] result;
    
    public Drop[] getResult() {
        return result;
    }
    
    public void setResult(Drop[] result) {
        this.result = result;
    }

}
