package es.udc.lbd.hermes.model.gps;

import java.util.Calendar;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Point;

import es.udc.lbd.hermes.model.util.jackson.CustomGeometrySerializer;
import es.udc.lbd.hermes.model.util.jackson.CustomPointDeserializer;

public class GpsDTO {

	private Long id;
	
	private Calendar time;
	
	@JsonSerialize(using = CustomGeometrySerializer.class)
	@JsonDeserialize(using = CustomPointDeserializer.class)	
	private Point position;
	private String userId;
	
	private String provider;
	
	private Double altitude;
	
	private Double speed;
	
	private Double bearing;
	
	private Double accuracy;

	public GpsDTO(){}
	
	public GpsDTO(Long id, Calendar time, Point position, String userId,
			String provider, Double altitude, Double speed, Double bearing,
			Double accuracy) {
		super();
		this.id = id;
		this.time = time;
		this.position = position;
		this.userId = userId;
		this.provider = provider;
		this.altitude = altitude;
		this.speed = speed;
		this.bearing = bearing;
		this.accuracy = accuracy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Calendar getTime() {
		return time;
	}

	public void setTime(Calendar time) {
		this.time = time;
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

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
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
