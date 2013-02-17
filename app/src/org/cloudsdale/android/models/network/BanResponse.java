package org.cloudsdale.android.models.network;

import org.cloudsdale.android.models.api.Ban;

import java.util.ArrayList;

import lombok.Data;

@Data
public class BanResponse extends Response {

	private Ban result;
	private ArrayList<Ban> results;
	
}
