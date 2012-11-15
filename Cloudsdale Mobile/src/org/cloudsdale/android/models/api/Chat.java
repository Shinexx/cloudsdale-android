package org.cloudsdale.android.models.api;

import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.cloudsdale.android.models.Model;

import com.google.gson.annotations.Expose;

@Data
@EqualsAndHashCode(callSuper=false)
public class Chat extends Model {

	private Cloud				cloud;
	private ArrayList<Message>	messages;
	private String				token;

}
