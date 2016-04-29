package es.udc.lbd.hermes.model.events.driverFeatures;

import java.util.Calendar;

public class DriverFeaturesDTO {

	private Long id;
	private Integer awakeFor;
	private Integer inBed;
	private Integer workingTime;
	private Integer deepSleep;
	private Integer previousStress;
	private Integer lightSleep;
	private Calendar timeStamp;
	private String userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Integer getLightSleep() {
		return lightSleep;
	}

	public void setLightSleep(Integer lightSleep) {
		this.lightSleep = lightSleep;
	}

	public Calendar getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Calendar timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
