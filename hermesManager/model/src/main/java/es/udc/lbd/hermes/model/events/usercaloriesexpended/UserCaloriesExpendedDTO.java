package es.udc.lbd.hermes.model.events.usercaloriesexpended;

import java.util.Calendar;

public class UserCaloriesExpendedDTO {

	private Long id;
	private Calendar startTime;
	private Calendar endTime;
	private Float calories;
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
	public Float getCalories() {
		return calories;
	}
	public void setCalories(Float calories) {
		this.calories = calories;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	
}
