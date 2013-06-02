package org.cloudsdale.android.models.network;

import org.cloudsdale.android.models.api.Drop;

import java.util.ArrayList;

public class DropResponse extends Response {

	private Drop result;
	private ArrayList<Drop> results;

    public Drop getResult() {
        return result;
    }

    public void setResult(Drop result) {
        this.result = result;
    }

    public ArrayList<Drop> getResults() {
        return results;
    }

    public void setResults(ArrayList<Drop> results) {
        this.results = results;
    }
}
