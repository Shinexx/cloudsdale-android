package org.cloudsdale.model.v2;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tyr on 19/09/2013.
 */
public class User extends BaseModel {

    private String  username;
    private String  avatar;
    @SerializedName("read_terms")
    private boolean readTerms;
    @SerializedName("default_avatar")
    private boolean defaultAvatar;
    private String  email;
    @SerializedName("email_varified")
    private boolean emailVerified;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean hasReadTerms() {
        return readTerms;
    }

    public void setReadTerms(boolean readTerms) {
        this.readTerms = readTerms;
    }

    public boolean isDefaultAvatar() {
        return defaultAvatar;
    }

    public void setDefaultAvatar(boolean defaultAvatar) {
        this.defaultAvatar = defaultAvatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
}
