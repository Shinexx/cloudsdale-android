package org.cloudsdale.android.models.network;

import org.cloudsdale.android.models.api.User;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse extends Response {

	private User result;
	private ArrayList<User> results;
	
}
