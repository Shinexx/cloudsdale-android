package org.cloudsdale.android.models.api_models;

import org.cloudsdale.android.models.IdentityModel;

public class Drop extends IdentityModel {
    
    private String url;
    private String[] status;
    private String title;
    private String preview;
    
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String[] getStatus() {
        return status;
    }
    public void setStatus(String[] status) {
        this.status = status;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getPreview() {
        return preview;
    }
    public void setPreview(String preview) {
        this.preview = preview;
    }
}
