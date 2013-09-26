package org.cloudsdale.model.v2;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by tyr on 19/09/2013.
 */
@Data
public class Cloud extends BaseModel {

    @SerializedName("display_name")
    private String  displayName;
    @SerializedName("short_name")
    private String  shortName;
    private String  avatar;
    @SerializedName("participant_count")
    private Integer participantCount;
    private Boolean hidden;
    private Boolean locked;

}
