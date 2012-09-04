package org.cloudsdale.android.models.network_models;

import org.cloudsdale.android.models.api_models.Cloud;

public class ApiCloudResponse extends ApiResponse {

    private Cloud result;

    public Cloud getResult() {
        return this.result;
    }

    public void setResult(Cloud result) {
        this.result = result;
    }

}
