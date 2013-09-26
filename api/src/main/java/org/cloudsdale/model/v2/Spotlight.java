package org.cloudsdale.model.v2;

import lombok.Data;

/**
 * Created by tyr on 19/09/2013.
 */
@Data
public class Spotlight extends BaseModel {

    private String text;
    private String category;
    private Target target;

    @Data
    public class Target {

        private Type   type;
        private String id;

    }

}
