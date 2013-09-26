package org.cloudsdale.response.v2;

import org.cloudsdale.model.v2.User;

/**
 * Created by tyr on 25/09/2013.
 */
public class UserCollectionResponse extends CollectionResponse {

    private User[] users;

    public User[] getUsers() {
        return users;
    }
}
