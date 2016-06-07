package es.udc.lbd.hermes.model.events;

import java.io.Serializable;
import java.util.List;

import es.udc.lbd.hermes.model.events.usersleep.UserSleepDTO;

public class ListaUserSleep implements Serializable{

	private static final long serialVersionUID = -2974886731443596376L;
	
	private Long totalResults;
	private Integer returnedResults;
	private List<UserSleepDTO> results;
		
	public ListaUserSleep(){}

	public ListaUserSleep(Long totalResults, Integer returnedResults,
			List<UserSleepDTO> results) {
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

	public List<UserSleepDTO> getResults() {
		return results;
	}

	public void setResults(List<UserSleepDTO> results) {
		this.results = results;
	}
	
}
