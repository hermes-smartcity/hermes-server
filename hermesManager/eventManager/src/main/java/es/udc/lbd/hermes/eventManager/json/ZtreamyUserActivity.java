package es.udc.lbd.hermes.eventManager.json;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZtreamyUserActivity extends EventData {
	
	@JsonProperty("endTime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private Calendar endTime;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("startTime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private Calendar startTime;

	public Calendar getEndTime() {
		return endTime;
	}

	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}
}
