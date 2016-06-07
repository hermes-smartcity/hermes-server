package es.udc.lbd.hermes.eventManager.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZtreamyUserCaloriesExpendedList extends EventData {

	@JsonProperty("userCaloriesExpendedList")
	private List<ZtreamyUserCaloriesExpended> userCaloriesExpendedList;

	public List<ZtreamyUserCaloriesExpended> getUserCaloriesExpendedList() {
		return userCaloriesExpendedList;
	}

	public void setUserCaloriesExpendedList(List<ZtreamyUserCaloriesExpended> userCaloriesExpendedList) {
		this.userCaloriesExpendedList = userCaloriesExpendedList;
	}
}
