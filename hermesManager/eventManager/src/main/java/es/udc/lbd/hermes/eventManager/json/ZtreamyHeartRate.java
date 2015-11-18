package es.udc.lbd.hermes.eventManager.json;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ZtreamyHeartRate {
	@JsonProperty("timeLog")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private Calendar timeLog;
	@JsonProperty("heartRate")
	private Integer heartRate;

	public Calendar getTimeLog() {
		return timeLog;
	}

	public void setTimeLog(Calendar timeLog) {
		this.timeLog = timeLog;
	}

	public Integer getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(Integer heartRate) {
		this.heartRate = heartRate;
	}
}
