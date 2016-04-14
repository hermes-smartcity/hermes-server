package es.udc.lbd.hermes.model.events.measurement;

import java.util.Calendar;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Point;

import es.udc.lbd.hermes.model.util.jackson.CustomGeometrySerializer;
import es.udc.lbd.hermes.model.util.jackson.CustomPointDeserializer;

public class MeasurementDTO {

	private Long id;
	private Calendar timestamp;
	@JsonSerialize(using = CustomGeometrySerializer.class)
	@JsonDeserialize(using = CustomPointDeserializer.class)		
	private Point position;

	private String measurementType;
	private Double value;
	private Double accuracy;
	private Double speed;
	private String tipo;
	private String userId;

	public MeasurementDTO(){}

	public MeasurementDTO(Long id, Calendar timestamp, Point position,
			String measurementType, Double value, Double accuracy,
			Double speed, String tipo, String userId) {
		super();
		this.id = id;
		this.timestamp = timestamp;
		this.position = position;
		this.measurementType = measurementType;
		this.value = value;
		this.accuracy = accuracy;
		this.speed = speed;
		this.tipo = tipo;
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Calendar getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public String getMeasurementType() {
		return measurementType;
	}

	public void setMeasurementType(String measurementType) {
		this.measurementType = measurementType;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Double accuracy) {
		this.accuracy = accuracy;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


}
