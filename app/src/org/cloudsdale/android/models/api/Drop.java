package org.cloudsdale.android.models.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.cloudsdale.android.models.IdentityModel;

@Data
@EqualsAndHashCode(callSuper=false)
public class Drop extends IdentityModel {
    
    private String url;
    private String[] status;
    private String title;
    private String preview;
    
}
