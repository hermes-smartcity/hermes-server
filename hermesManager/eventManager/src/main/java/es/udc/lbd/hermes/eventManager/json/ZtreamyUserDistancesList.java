package es.udc.lbd.hermes.eventManager.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZtreamyUserDistancesList extends EventData {

	@JsonProperty("userDistancesList")
	private List<ZtreamyUserDistances> userDistancesList;

	public List<ZtreamyUserDistances> getUserDistancesList() {
		return userDistancesList;
	}

	public void setUserDistancesList(List<ZtreamyUserDistances> userDistancesList) {
		this.userDistancesList = userDistancesList;
	}
}
