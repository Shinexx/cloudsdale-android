package org.cloudsdale.response.v2;

import org.cloudsdale.model.v2.User;

import lombok.Data;

/**
 * Created by tyr on 25/09/2013.
 */
@Data
public class UserCollectionResponse extends CollectionResponse {

    private User[] users;

}
