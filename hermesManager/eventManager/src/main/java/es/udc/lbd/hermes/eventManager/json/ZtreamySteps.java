package es.udc.lbd.hermes.eventManager.json;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ZtreamySteps {
	@JsonProperty("timeLog")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss")
	private Calendar timeLog;
	@JsonProperty("steps")
	private Integer steps;
	public Calendar getTimeLog() {
		return timeLog;
	}
	public void setTimeLog(Calendar timeLog) {
		this.timeLog = timeLog;
	}
	public Integer getSteps() {
		return steps;
	}
	public void setSteps(Integer steps) {
		this.steps = steps;
	}
	
}
