package org.cloudsdale.response.v2;

import lombok.Data;

/**
 * Created by tyr on 25/09/2013.
 */
@Data
public abstract class CollectionResponse {

    private Collection collection;

    @Data
    public class Collection {

        private Relative next;
        private Relative prev;

    }

    @Data
    public class Relative {

        private int time;
        private int limit;
        private int offest;

    }

}
