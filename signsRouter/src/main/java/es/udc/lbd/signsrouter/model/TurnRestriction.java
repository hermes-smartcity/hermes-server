package es.udc.lbd.signsrouter.model;

public class TurnRestriction {
	public Edge origin;
	public Edge dest;
	
	public TurnRestriction(Edge origin, Edge dest) {
		super();
		this.origin = origin;
		this.dest = dest;
	}

	@Override
	public String toString() {
		return "TurnRestriction [origin=" + origin + ", dest=" + dest + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dest == null) ? 0 : dest.hashCode());
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
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
		TurnRestriction other = (TurnRestriction) obj;
		if (dest == null) {
			if (other.dest != null)
				return false;
		} else if (!dest.equals(other.dest))
			return false;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		return true;
	}
	
	
}
