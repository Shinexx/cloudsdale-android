package org.cloudsdale.models.v1;

/**
 * Model class to pass on identity properties to child models
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class IdentityModel extends Model {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    protected String	id;

}
