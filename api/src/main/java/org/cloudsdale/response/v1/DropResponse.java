package org.cloudsdale.response.v1;

import org.cloudsdale.model.v1.Drop;

import java.util.List;

public class DropResponse extends Response {

	private Drop result;
	private List<Drop> results;

    public Drop getResult() {
        return result;
    }

    public void setResult(Drop result) {
        this.result = result;
    }

    public List<Drop> getResults() {
        return results;
    }

    public void setResults(List<Drop> results) {
        this.results = results;
    }
}
