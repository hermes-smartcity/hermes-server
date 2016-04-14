package es.udc.lbd.hermes.model.events.contextData;

import java.util.Calendar;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Point;

import es.udc.lbd.hermes.model.util.jackson.CustomGeometrySerializer;
import es.udc.lbd.hermes.model.util.jackson.CustomPointDeserializer;

public class ContextDataDTO {

	private Long id;
	private Calendar timeLog;

	private String detectedActivity;

	private Integer accuracy;
	@JsonSerialize(using = CustomGeometrySerializer.class)
	@JsonDeserialize(using = CustomPointDeserializer.class)	
	private Point position;
	private String userId;

	public ContextDataDTO(){}

	public ContextDataDTO(Long id, Calendar timeLog, String detectedActivity,
			Integer accuracy, Point position, String userId) {
		super();
		this.id = id;
		this.timeLog = timeLog;
		this.detectedActivity = detectedActivity;
		this.accuracy = accuracy;
		this.position = position;
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Calendar getTimeLog() {
		return timeLog;
	}

	public void setTimeLog(Calendar timeLog) {
		this.timeLog = timeLog;
	}

	public String getDetectedActivity() {
		return detectedActivity;
	}

	public void setDetectedActivity(String detectedActivity) {
		this.detectedActivity = detectedActivity;
	}

	public Integer getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Integer accuracy) {
		this.accuracy = accuracy;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


}
