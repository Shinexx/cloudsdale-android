package org.cloudsdale.android.models.network;

import org.cloudsdale.android.models.api.Cloud;

import java.util.ArrayList;

public class CloudResponse extends Response {

	private Cloud result;
	private ArrayList<Cloud> results;

    public Cloud getResult() {
        return result;
    }

    public void setResult(Cloud result) {
        this.result = result;
    }

    public ArrayList<Cloud> getResults() {
        return results;
    }

    public void setResults(ArrayList<Cloud> results) {
        this.results = results;
    }
}
