package es.udc.lbd.hermes.model.events;

import java.io.Serializable;
import java.util.List;

import es.udc.lbd.hermes.model.events.stepsData.StepsDataDTO;

public class ListaStepsData implements Serializable{

	private static final long serialVersionUID = -2974886731443596376L;
	
	private Long totalResults;
	private Integer returnedResults;
	private List<StepsDataDTO> results;
		
	public ListaStepsData(){}

	public ListaStepsData(Long totalResults, Integer returnedResults,
			List<StepsDataDTO> results) {
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

	public List<StepsDataDTO> getResults() {
		return results;
	}

	public void setResults(List<StepsDataDTO> results) {
		this.results = results;
	}
	
}
