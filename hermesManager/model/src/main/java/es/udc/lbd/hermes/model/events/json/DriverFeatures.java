package es.udc.lbd.hermes.model.events.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DriverFeatures extends EventData {

	@JsonProperty("AwakeFor")
	private Integer awakeFor;
	@JsonProperty("In bed")
	private Integer inBed;
	@JsonProperty("Working Time")
	private Integer workingTime;
	@JsonProperty("Light Sleep")
	private Integer lightSleep;
	@JsonProperty("Deep Sleep")
	private Integer deepSleep;
	
	public Integer getAwakeFor() {
		return awakeFor;
	}
	public void setAwakeFor(Integer awakeFor) {
		this.awakeFor = awakeFor;
	}
	public Integer getInBed() {
		return inBed;
	}
	public void setInBed(Integer inBed) {
		this.inBed = inBed;
	}
	public Integer getWorkingTime() {
		return workingTime;
	}
	public void setWorkingTime(Integer workingTime) {
		this.workingTime = workingTime;
	}
	public Integer getLightSleep() {
		return lightSleep;
	}
	public void setLightSleep(Integer lightSleep) {
		this.lightSleep = lightSleep;
	}
	public Integer getDeepSleep() {
		return deepSleep;
	}
	public void setDeepSleep(Integer deepSleep) {
		this.deepSleep = deepSleep;
	}
	
	
}
