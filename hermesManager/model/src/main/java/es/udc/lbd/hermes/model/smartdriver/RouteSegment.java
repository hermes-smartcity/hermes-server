package es.udc.lbd.hermes.model.smartdriver;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.LineString;

import es.udc.lbd.hermes.model.util.jackson.CustomGeometrySerializer;
import es.udc.lbd.hermes.model.util.jackson.CustomMultiLineStringDeserializer;

public class RouteSegment {

	private Long linkId;
	private Integer maxSpeed;
	private String linkName;
	private String linkType;
	private Double length;
	private Double cost;
	
	@Type(type="org.hibernate.spatial.GeometryType")
	@JsonSerialize(using = CustomGeometrySerializer.class)
    @JsonDeserialize(using = CustomMultiLineStringDeserializer.class)     
	private LineString geom_way;
	
	public RouteSegment(){}

	public RouteSegment(Long linkId, Integer maxSpeed, String linkName,
			String linkType, Double length, Double cost, LineString geom_way) {
		super();
		this.linkId = linkId;
		this.maxSpeed = maxSpeed;
		this.linkName = linkName;
		this.linkType = linkType;
		this.length = length;
		this.cost = cost;
		this.geom_way = geom_way;
	}

	public Long getLinkId() {
		return linkId;
	}

	public void setLinkId(Long linkId) {
		this.linkId = linkId;
	}

	public Integer getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(Integer maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public LineString getGeom_way() {
		return geom_way;
	}

	public void setGeom_way(LineString geom_way) {
		this.geom_way = geom_way;
	}
	
}
