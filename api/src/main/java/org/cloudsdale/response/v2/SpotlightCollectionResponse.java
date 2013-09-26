package org.cloudsdale.response.v2;

import org.cloudsdale.model.v2.Cloud;
import org.cloudsdale.model.v2.Spotlight;

/**
 * Created by tyr on 25/09/2013.
 */
public class SpotlightCollectionResponse extends CollectionResponse {

    private Cloud[]     clouds;
    private Spotlight[] spotlights;

    public Cloud[] getClouds() {
        return clouds;
    }

    public Spotlight[] getSpotlights() {
        return spotlights;
    }
}
