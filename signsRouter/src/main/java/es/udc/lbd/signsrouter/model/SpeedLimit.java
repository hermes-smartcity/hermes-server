package es.udc.lbd.signsrouter.model;

import java.util.HashSet;
import java.util.Set;

public class SpeedLimit implements Comparable<SpeedLimit> {
	
	public final static SpeedLimit DEFAULT_SPEED = new SpeedLimit(0); 

	public int speed;
	public Set<Edge> edges;
	
	public SpeedLimit(int speed) {
		super();
		this.speed = speed;
		this.edges = new HashSet<Edge>(10);
	}

	@Override
	public String toString() {
		return "SpeedLimit [speed=" + speed + ", edges=" + edges + "]";
	}

	public int compareTo(SpeedLimit o) {
		if (this == o) return 0;
		if (this == DEFAULT_SPEED) return 1;
		if (o == DEFAULT_SPEED) return -1;
		return speed - o.speed;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((edges == null) ? 0 : edges.hashCode());
//		result = prime * result + speed;
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		SpeedLimit other = (SpeedLimit) obj;
//		if (edges == null) {
//			if (other.edges != null)
//				return false;
//		} else if (!edges.equals(other.edges))
//			return false;
//		if (speed != other.speed)
//			return false;
//		return true;
//	}
	
	
	
}
