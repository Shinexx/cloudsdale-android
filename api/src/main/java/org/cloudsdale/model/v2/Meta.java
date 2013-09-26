package org.cloudsdale.model.v2;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tyr on 19/09/2013.
 */
public class Meta {

    private int version;
    private String  description;
    private String  copyright;
    private String  website;
    @SerializedName("terms_of_use")
    private String  termsOfUseUrl;
    @SerializedName("privacy_policy")
    private String  privacyPolicyUrl;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTermsOfUseUrl() {
        return termsOfUseUrl;
    }

    public void setTermsOfUseUrl(String termsOfUseUrl) {
        this.termsOfUseUrl = termsOfUseUrl;
    }

    public String getPrivacyPolicyUrl() {
        return privacyPolicyUrl;
    }

    public void setPrivacyPolicyUrl(String privacyPolicyUrl) {
        this.privacyPolicyUrl = privacyPolicyUrl;
    }

}
