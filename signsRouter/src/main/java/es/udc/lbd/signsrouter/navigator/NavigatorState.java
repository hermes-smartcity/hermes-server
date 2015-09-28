package es.udc.lbd.signsrouter.navigator;

import es.udc.lbd.signsrouter.model.Edge;
import es.udc.lbd.signsrouter.model.SpeedLimit;

public class NavigatorState {

	public Edge edge;
	public SpeedLimit speedLimit;
	
	public NavigatorState(Edge edge, SpeedLimit speedLimit) {
		super();
		this.edge = edge;
		this.speedLimit = speedLimit;
	}
	
	public int limitSpeed(int newLimit) {
		if (speedLimit == null || speedLimit.speed <= 0 || speedLimit.speed > newLimit)
			speedLimit = new SpeedLimit(newLimit);
		
		return speedLimit.speed;
	}

	@Override
	public String toString() {
		return "NavigatorState [edge=" + edge + ", speedLimit=" + speedLimit + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((edge == null) ? 0 : edge.hashCode());
		result = prime * result
				+ ((speedLimit == null) ? 0 : speedLimit.hashCode());
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
		NavigatorState other = (NavigatorState) obj;
		if (edge == null) {
			if (other.edge != null)
				return false;
		} else if (!edge.equals(other.edge))
			return false;
		if (speedLimit == null) {
			if (other.speedLimit != null)
				return false;
		} else if (!speedLimit.equals(other.speedLimit))
			return false;
		return true;
	}
	
	
}
