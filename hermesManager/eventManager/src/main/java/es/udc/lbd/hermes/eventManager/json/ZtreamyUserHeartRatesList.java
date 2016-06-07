package es.udc.lbd.hermes.eventManager.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZtreamyUserHeartRatesList extends EventData {

	@JsonProperty("userHeartRatesList")
	private List<ZtreamyUserHeartRates> userHeartRatesList;

	public List<ZtreamyUserHeartRates> getUserHeartRatesList() {
		return userHeartRatesList;
	}

	public void setUserHeartRatesList(List<ZtreamyUserHeartRates> userHeartRatesList) {
		this.userHeartRatesList = userHeartRatesList;
	}
}
