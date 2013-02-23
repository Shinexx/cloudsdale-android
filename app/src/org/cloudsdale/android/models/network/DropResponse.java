package org.cloudsdale.android.models.network;

import org.cloudsdale.android.models.api.Drop;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DropResponse extends Response {

	private Drop result;
	private ArrayList<Drop> results;
	
}
