package es.udc.lbd.hermes.eventManager.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZtreamyUserSleepList extends EventData {

	@JsonProperty("userSleepList")
	private List<ZtreamyUserSleep> userSleepList;

	public List<ZtreamyUserSleep> getUserSleepList() {
		return userSleepList;
	}

	public void setUserSleepList(List<ZtreamyUserSleep> userSleepList) {
		this.userSleepList = userSleepList;
	}
}
