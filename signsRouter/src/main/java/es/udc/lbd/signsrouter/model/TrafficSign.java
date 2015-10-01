package es.udc.lbd.signsrouter.model;

import com.vividsolutions.jts.geom.Coordinate;

public class TrafficSign {
	public Coordinate position;
	public float azimut;
	public String type;
	
	public TrafficSign(Coordinate position, float azimut, String type) {
		super();
		this.position = position;
		this.azimut = azimut;
		this.type = type.toUpperCase();
	}
	
	public boolean noWay() {
		return type.equals("R101");
	}
	
	/**
	 * 
	 * @param relativeHeading a deviation from -180 to 180 from the original way heading for this turn.
	 * 	Positive means right.
	 */
	public boolean turnRestriction(double relativeHeading) {
		if (type.equals("R303")) {
			return relativeHeading < -10 && relativeHeading > -140;
		}
		
		if (type.equals("R302")) {
			return relativeHeading > 10 && relativeHeading < 140;
		}
		
		return false;
	}
	
	public int speedLimit() {
		if (type.startsWith("R301")) {
			return Integer.parseInt(type.split("-")[1]);
		} else {
			return 0;
		}
	}
	
	public boolean interruptsSpeed() {
		return "R1".equals(type);
	}

	@Override
	public String toString() {
		return "TrafficSign [position=" + position + ", azimut=" + azimut + ", type=" + type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(azimut);
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TrafficSign other = (TrafficSign) obj;
		if (Float.floatToIntBits(azimut) != Float.floatToIntBits(other.azimut))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	
}
