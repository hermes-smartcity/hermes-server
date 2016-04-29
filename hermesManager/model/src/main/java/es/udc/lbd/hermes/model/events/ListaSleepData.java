package es.udc.lbd.hermes.model.events;

import java.io.Serializable;
import java.util.List;

import es.udc.lbd.hermes.model.events.sleepData.SleepDataDTO;

public class ListaSleepData implements Serializable{

	private static final long serialVersionUID = -2974886731443596376L;
	
	private Long totalResults;
	private Integer returnedResults;
	private List<SleepDataDTO> results;
		
	public ListaSleepData(){}

	public ListaSleepData(Long totalResults, Integer returnedResults,
			List<SleepDataDTO> results) {
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

	public List<SleepDataDTO> getResults() {
		return results;
	}

	public void setResults(List<SleepDataDTO> results) {
		this.results = results;
	}
	
}
