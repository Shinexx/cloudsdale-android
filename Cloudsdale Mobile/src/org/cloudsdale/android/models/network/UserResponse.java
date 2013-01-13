package org.cloudsdale.android.models.network;

import org.cloudsdale.android.models.api.User;

import java.util.ArrayList;

import lombok.Data;

@Data
public class UserResponse extends Response {

	private User result;
	private ArrayList<User> results;
	
}
