package es.udc.lbd;

public class TrafficSign {
	public Position position;
	public float azimut;
	public String type;
	
	public TrafficSign(Position position, float azimut, String type) {
		super();
		this.position = position;
		this.azimut = azimut;
		this.type = type;
	}
	
	public boolean noWay() {
		return type.toUpperCase().equals("R101");
	}

	@Override
	public String toString() {
		return "TrafficSign [position=" + position + ", azimut=" + azimut
				+ ", type=" + type + "]";
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
