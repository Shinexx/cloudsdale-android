package org.cloudsdale.model.v2;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by tyr on 19/09/2013.
 */
@Data
public abstract class BaseModel {

    private String          id;
    private Type            type;
    @SerializedName("created_at")
    private Date            createdAt;
    @SerializedName("updated_at")
    private Date            updatedAt;
    private Boolean         deleted;
    @SerializedName("transient")
    private Boolean         ofTransientStatus;
    @SerializedName("display_name")
    private String          displayName;

}
