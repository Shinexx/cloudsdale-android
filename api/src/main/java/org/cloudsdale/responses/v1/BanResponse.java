package org.cloudsdale.responses.v1;

import org.cloudsdale.models.v1.Ban;

import java.util.List;

public class BanResponse extends Response {

	private Ban			result;
	private List<Ban>	results;

	public Ban getResult() {
		return result;
	}

	public void setResult(Ban result) {
		this.result = result;
	}

	public List<Ban> getResults() {
		return results;
	}

	public void setResults(List<Ban> results) {
		this.results = results;
	}
}
