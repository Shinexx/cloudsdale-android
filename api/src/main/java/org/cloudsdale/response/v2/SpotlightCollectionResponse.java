package org.cloudsdale.response.v2;

import lombok.Data;

import org.cloudsdale.model.v2.Cloud;
import org.cloudsdale.model.v2.Spotlight;

/**
 * Created by tyr on 25/09/2013.
 */
@Data
public class SpotlightCollectionResponse extends CollectionResponse {

    private Cloud[]     clouds;
    private Spotlight[] spotlights;

}
