package es.udc.lbd.hermes.model.events.sleepData;

import java.util.Calendar;

public class SleepDataDTO {

	private Long id;
	private String eventId;
	private Integer awakenings;
	private Integer minutesAsleep;
	private Integer minutesInBed;
	private Calendar startTime;
	private Calendar endTime;
	private String userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
