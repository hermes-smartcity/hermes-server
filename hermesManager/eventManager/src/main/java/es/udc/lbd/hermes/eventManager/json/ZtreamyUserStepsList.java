package es.udc.lbd.hermes.eventManager.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZtreamyUserStepsList extends EventData {

	@JsonProperty("userStepsList")
	private List<ZtreamyUserSteps> userStepsList;

	public List<ZtreamyUserSteps> getUserStepsList() {
		return userStepsList;
	}

	public void setUserStepsList(List<ZtreamyUserSteps> userStepsList) {
		this.userStepsList = userStepsList;
	}
}
