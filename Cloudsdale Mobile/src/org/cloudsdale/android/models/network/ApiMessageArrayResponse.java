package org.cloudsdale.android.models.network;

import com.google.gson.annotations.Expose;

import org.cloudsdale.android.models.api.Message;

public class ApiMessageArrayResponse extends ApiResponse {

	@Expose
    private Message[] result;

    public Message[] getResult() {
        return result;
    }

    public void setResult(Message[] result) {
        this.result = result;
    }

}
