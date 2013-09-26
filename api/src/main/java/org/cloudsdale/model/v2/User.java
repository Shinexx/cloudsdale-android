package org.cloudsdale.model.v2;

import com.google.gson.annotations.SerializedName;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

/**
 * Created by tyr on 19/09/2013.
 */
@Data
public class User extends BaseModel {

    private String  username;
    private String  avatar;
    @SerializedName("read_terms")
    @Getter(AccessLevel.NONE)
    private Boolean readTerms;
    @SerializedName("default_avatar")
    private Boolean defaultAvatar;
    private String  email;
    @SerializedName("email_varified")
    private Boolean emailVerified;

    public Boolean hasReadTerms() {
        return readTerms;
    }
}
