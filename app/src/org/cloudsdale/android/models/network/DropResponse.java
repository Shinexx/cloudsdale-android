package org.cloudsdale.android.models.network;

import org.cloudsdale.android.models.api.Drop;

import java.util.ArrayList;

import lombok.Data;

@Data
public class DropResponse extends Response {

	private Drop result;
	private ArrayList<Drop> results;
	
}
