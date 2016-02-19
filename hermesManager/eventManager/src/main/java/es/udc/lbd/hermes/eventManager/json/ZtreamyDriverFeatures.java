package es.udc.lbd.hermes.eventManager.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZtreamyDriverFeatures extends EventData {

	@JsonProperty("awake")
	private Integer awakeFor;
	@JsonProperty("inbed")
	private Integer inBed;
	@JsonProperty("workingTime")
	private Integer workingTime;
	@JsonProperty("lightSleep")
	private Integer lightSleep;
	@JsonProperty("deepSleep")
	private Integer deepSleep;
	@JsonProperty("previousStress")
	private Integer previousStress;
	
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
	public Integer getPreviousStress() {
		return previousStress;
	}
	public void setPreviousStress(Integer previousStress) {
		this.previousStress = previousStress;
	}
	
}
