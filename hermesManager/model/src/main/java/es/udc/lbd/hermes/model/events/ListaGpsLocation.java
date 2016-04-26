package es.udc.lbd.hermes.model.events;

import java.io.Serializable;
import java.util.List;

import es.udc.lbd.hermes.model.gps.GpsDTO;

public class ListaGpsLocation implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long totalResults;
	private Integer returnedResults;
	private List<GpsDTO> results;
		
	public ListaGpsLocation(){}

	public ListaGpsLocation(Long totalResults, Integer returnedResults,
			List<GpsDTO> results) {
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

	public List<GpsDTO> getResults() {
		return results;
	}

	public void setResults(List<GpsDTO> results) {
		this.results = results;
	}
	
}
