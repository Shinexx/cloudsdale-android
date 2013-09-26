package org.cloudsdale.model.v2;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by tyr on 19/09/2013.
 */
@Data
public class Meta {

    private Integer version;
    private String  description;
    private String  copyright;
    private String  website;
    @SerializedName("terms_of_use")
    private String  termsOfUseUrl;
    @SerializedName("privacy_policy")
    private String  privacyPolicyUrl;

}
