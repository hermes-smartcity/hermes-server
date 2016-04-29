package es.udc.lbd.hermes.model.events.heartRateData;

import java.util.Calendar;

public class HeartRateDataDTO {

	private Long id;
	private String eventId;
	private Calendar timeLog;
	private Integer heartRate;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
