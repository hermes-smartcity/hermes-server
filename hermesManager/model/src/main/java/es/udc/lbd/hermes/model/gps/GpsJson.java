package es.udc.lbd.hermes.model.gps;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GpsJson {

	@JsonProperty("time")
	private long time;
	
	@JsonProperty("longitude")
	private Double longitude;
	
	@JsonProperty("latitude")
	private Double latitude;
	
	@JsonProperty("altitude")
	private Double altitude;
	
	@JsonProperty("speed")
	private Double speed;
	
	@JsonProperty("bearing")
	private Double bearing;
	
	@JsonProperty("accuracy")
	private Double accuracy;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getAltitude() {
		return altitude;
	}

	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Double getBearing() {
		return bearing;
	}

	public void setBearing(Double bearing) {
		this.bearing = bearing;
	}

	public Double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Double accuracy) {
		this.accuracy = accuracy;
	}
	
	
	
}
