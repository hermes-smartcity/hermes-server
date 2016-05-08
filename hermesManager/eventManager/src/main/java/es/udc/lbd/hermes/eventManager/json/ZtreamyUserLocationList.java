package es.udc.lbd.hermes.eventManager.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZtreamyUserLocationList extends EventData {

	@JsonProperty("userLocationsList")
	private List<ZtreamyUserLocation> userLocationsList;

	public List<ZtreamyUserLocation> getUserLocationsList() {
		return userLocationsList;
	}

	public void setUserLocationsList(List<ZtreamyUserLocation> userLocationsList) {
		this.userLocationsList = userLocationsList;
	}
}
