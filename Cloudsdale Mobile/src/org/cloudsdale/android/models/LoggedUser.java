package org.cloudsdale.android.models;

import org.cloudsdale.android.models.api.User;

public class LoggedUser extends User {

    public LoggedUser(User user, String authToken) {
        super(user.getName(), user.getTimeZone(), user.getMemberSinceTemp(),
                user.getMemberSince(), null, user.getSuspendedUntil(), user
                        .getReasonForSuspension(), user.getAvatar(), user
                        .isRegistered(), user.isTransientStatus(), user
                        .isBanStatus(), user.isMemberOfACloud(), user
                        .isHasAvatar(), user.isHasReadTnC(),
                user.getRoleTemp(), user.getRole(), user.getProsecutions(),
                user.getAuthToken(), user.getEmail(), user
                        .isNeedsToConfirmRegistration(), user
                        .isNeedsToChangePassword(), user.isNeedsToChangeName(),
                user.getClouds());
        this.authToken = authToken;
    }

    private String clientId;

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

}
