package es.udc.lbd.hermes.model.events;

import java.io.Serializable;
import java.util.List;

import es.udc.lbd.hermes.model.events.measurement.Measurement;

public class ListaMeasurement implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long totalResults;
	private Integer returnedResults;
	private List<Measurement> results;
		
	public ListaMeasurement(){}

	public ListaMeasurement(Long totalResults, Integer returnedResults,
			List<Measurement> results) {
		super();
		this.totalResults = totalResults;
		this.returnedResults = returnedResults;
		this.results = results;
	}

	public Long getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(Long totalResults) {
		this.totalResults = totalResults;
	}

	public Integer getReturnedResults() {
		return returnedResults;
	}

	public void setReturnedResults(Integer returnedResults) {
		this.returnedResults = returnedResults;
	}

	public List<Measurement> getResults() {
		return results;
	}

	public void setResults(List<Measurement> results) {
		this.results = results;
	}
	
}
