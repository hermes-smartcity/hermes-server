package es.udc.lbd.hermes.eventManager.json;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ZtreamyUserCaloriesExpended extends EventData {

	@JsonProperty("endTime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private Calendar endTime;	
		
	@JsonProperty("calories")
	private Float calories;
	
	@JsonProperty("startTime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private Calendar startTime;

	public Calendar getEndTime() {
		return endTime;
	}

	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}

	public Float getCalories() {
		return calories;
	}

	public void setCalories(Float calories) {
		this.calories = calories;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}	
	
	
}
