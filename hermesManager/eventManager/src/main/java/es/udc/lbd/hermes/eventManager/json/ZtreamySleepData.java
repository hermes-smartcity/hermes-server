package es.udc.lbd.hermes.eventManager.json;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZtreamySleepData extends EventData {
	@JsonProperty("awakenings")
	private Integer awakenings;
	@JsonProperty("minutesAsleep")
	private Integer minutesAsleep;
	@JsonProperty("minutesInBed")
	private Integer minutesInBed;
	@JsonProperty("dateTime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Calendar dateTime;
	@JsonProperty("startTime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private Calendar startTime;
	@JsonProperty("endTime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private Calendar endTime;

	public Integer getAwakenings() {
		return awakenings;
	}

	public void setAwakenings(Integer awakenings) {
		this.awakenings = awakenings;
	}

	public Integer getMinutesAsleep() {
		return minutesAsleep;
	}

	public void setMinutesAsleep(Integer minutesAsleep) {
		this.minutesAsleep = minutesAsleep;
	}

	public Integer getMinutesInBed() {
		return minutesInBed;
	}

	public void setMinutesInBed(Integer minutesInBed) {
		this.minutesInBed = minutesInBed;
	}

	public Calendar getDateTime() {
		return dateTime;
	}

	public void setDateTime(Calendar dateTime) {
		this.dateTime = dateTime;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	public Calendar getEndTime() {
		return endTime;
	}

	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}
}
