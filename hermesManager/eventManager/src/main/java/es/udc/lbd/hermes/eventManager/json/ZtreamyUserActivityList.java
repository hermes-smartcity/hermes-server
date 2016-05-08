package es.udc.lbd.hermes.eventManager.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZtreamyUserActivityList extends EventData {

	@JsonProperty("userActivitiesList")
	private List<ZtreamyUserActivity> userActivitiesList;

	public List<ZtreamyUserActivity> getUserActivitiesList() {
		return userActivitiesList;
	}

	public void setUserActivitiesList(List<ZtreamyUserActivity> userActivitiesList) {
		this.userActivitiesList = userActivitiesList;
	}
}
