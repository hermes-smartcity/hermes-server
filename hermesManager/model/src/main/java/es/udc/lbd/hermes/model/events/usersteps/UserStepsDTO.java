package es.udc.lbd.hermes.model.events.usersteps;

import java.util.Calendar;

public class UserStepsDTO {

	private Long id;
	private Calendar startTime;
	private Calendar endTime;
	private Integer steps;
	private String userId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Integer getSteps() {
		return steps;
	}
	public void setSteps(Integer steps) {
		this.steps = steps;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	
}
