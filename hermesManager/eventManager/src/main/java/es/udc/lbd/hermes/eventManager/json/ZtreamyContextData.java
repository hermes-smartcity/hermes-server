package es.udc.lbd.hermes.eventManager.json;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZtreamyContextData {
	@JsonProperty("timeLog")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private Calendar timeLog;	
	@JsonProperty("latitude")
	private Double latitude;
	@JsonProperty("longitude")
	private Double longitude;
	@JsonProperty("accuracy")
	private Integer accuracy;
	@JsonProperty("detectedActivity")
	private String detectedActivity;
	public Calendar getTimeLog() {
		return timeLog;
	}
	public void setTimeLog(Calendar timeLog) {
		this.timeLog = timeLog;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Integer getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(Integer accuracy) {
		this.accuracy = accuracy;
	}
	public String getDetectedActivity() {
		return detectedActivity;
	}
	public void setDetectedActivity(String detectedActivity) {
		this.detectedActivity = detectedActivity;
	}
}
