package org.cloudsdale.response.v2;

import org.cloudsdale.model.v2.Cloud;

/**
 * Created by tyr on 25/09/2013.
 */
public class CloudCollectionResponse extends CollectionResponse {

    private Cloud[] clouds;

    public Cloud[] getClouds() {
        return clouds;
    }
}
