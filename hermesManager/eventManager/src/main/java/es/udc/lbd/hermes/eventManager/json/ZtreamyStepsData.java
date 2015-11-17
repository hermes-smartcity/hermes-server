package es.udc.lbd.hermes.eventManager.json;

import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ZtreamyStepsData extends EventData {

	@JsonProperty("dateTime")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="DD/MM/YYYY")
	private Calendar dateTime;
	@JsonProperty("stepsList")
	private List<ZtreamySteps> stepsList;
	
	public Calendar getDateTime() {
		return dateTime;
	}
	public void setDateTime(Calendar dateTime) {
		this.dateTime = dateTime;
	}
	public List<ZtreamySteps> getStepsList() {
		return stepsList;
	}
	public void setStepsList(List<ZtreamySteps> stepsList) {
		this.stepsList = stepsList;
	}
	
}
