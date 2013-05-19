package org.cloudsdale.android.models.network;

import org.cloudsdale.android.models.api.Ban;

import java.util.ArrayList;

public class BanResponse extends Response {

	private Ban result;
	private ArrayList<Ban> results;

    public Ban getResult() {
        return result;
    }

    public void setResult(Ban result) {
        this.result = result;
    }

    public ArrayList<Ban> getResults() {
        return results;
    }

    public void setResults(ArrayList<Ban> results) {
        this.results = results;
    }
}
