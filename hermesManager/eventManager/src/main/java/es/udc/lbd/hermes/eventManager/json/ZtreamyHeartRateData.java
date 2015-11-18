package es.udc.lbd.hermes.eventManager.json;

import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ZtreamyHeartRateData extends EventData {

	@JsonProperty("dateTime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Calendar dateTime;
	@JsonProperty("heartRateList")
	private List<ZtreamyHeartRate> heartRateList;

	public Calendar getDateTime() {
		return dateTime;
	}

	public void setDateTime(Calendar dateTime) {
		this.dateTime = dateTime;
	}

	public List<ZtreamyHeartRate> getHeartRateList() {
		return heartRateList;
	}

	public void setHeartRateList(List<ZtreamyHeartRate> heartRateList) {
		this.heartRateList = heartRateList;
	}
}
