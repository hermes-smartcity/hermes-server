package es.udc.lbd.hermes.model.smartdriver;

import java.io.Serializable;
import java.math.BigInteger;

public class NetworkLinkVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private BigInteger linkId;
	private Integer maxSpeed;
	private String linkName;
	private String linkType;
	private Double length;
	private Double position;
	private Double previousPosition;
	private Integer direction;
		
	public NetworkLinkVO(){}

	public NetworkLinkVO(BigInteger linkId, Integer maxSpeed, String linkName,
			String linkType, Double length, Double position,
			Double previousPosition, Integer direction) {
		super();
		this.linkId = linkId;
		this.maxSpeed = maxSpeed;
		this.linkName = linkName;
		this.linkType = linkType;
		this.length = length;
		this.position = position;
		this.previousPosition = previousPosition;
		this.direction = direction;
	}

	public BigInteger getLinkId() {
		return linkId;
	}

	public void setLinkId(BigInteger linkId) {
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

	public Double getPosition() {
		return position;
	}

	public void setPosition(Double position) {
		this.position = position;
	}

	public Double getPreviousPosition() {
		return previousPosition;
	}

	public void setPreviousPosition(Double previousPosition) {
		this.previousPosition = previousPosition;
	}

	public Integer getDirection() {
		return direction;
	}

	public void setDirection(Integer direction) {
		this.direction = direction;
	}

}
