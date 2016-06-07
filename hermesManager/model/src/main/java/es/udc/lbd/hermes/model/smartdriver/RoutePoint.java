package es.udc.lbd.hermes.model.smartdriver;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Point;

import es.udc.lbd.hermes.model.util.jackson.CustomGeometrySerializer;
import es.udc.lbd.hermes.model.util.jackson.CustomMultiLineStringDeserializer;

public class RoutePoint {

	private Double speed;
	@Type(type = "org.hibernate.spatial.GeometryType")
	@JsonSerialize(using = CustomGeometrySerializer.class)
	@JsonDeserialize(using = CustomMultiLineStringDeserializer.class)
	private Point position;

	public RoutePoint(Double speed, Point position) {
		super();
		this.speed = speed;
		this.position = position;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}
}
