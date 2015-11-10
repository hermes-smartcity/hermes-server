package es.udc.lbd.hermes.model.events.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoadSectionPoint {

	@JsonProperty("latitude")
	private Double latitude;
	@JsonProperty("longitude")
	private Double longitude;
	@JsonProperty("timeStamp")
	private Date timestamp;
	
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
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	
}
