package org.cloudsdale.android.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.cloudsdale.android.models.api.User;

@Data
@EqualsAndHashCode(callSuper=false)
public class LoggedUser extends User {

    private String clientId;

}
