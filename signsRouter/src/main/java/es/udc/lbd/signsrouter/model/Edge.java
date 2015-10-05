package es.udc.lbd.signsrouter.model;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Location;
import com.vividsolutions.jts.geomgraph.Label;

public class Edge extends com.vividsolutions.jts.geomgraph.DirectedEdge {
	public Long id;
	public Node origin;
	public Node dest;
	public boolean banned = false;
	private SpeedLimit speedLimit;
	public double ten_meters;
	
	public Edge(Long id, Node origin, Node dest, Coordinate posOrigin, Coordinate posDest, double ten_meters) {
		super(
				new com.vividsolutions.jts.geomgraph.Edge(
						new Coordinate[] {origin.getCoordinate(), posOrigin, posDest, dest.getCoordinate()}, new Label(Location.NONE)), true);
		
		if (id > 0)
			super.setSym(new Edge(-id, dest, origin, posDest, posOrigin, ten_meters));
		
		this.id = id;
		this.origin = origin;
		this.dest = dest;
		this.setSpeedLimit(SpeedLimit.DEFAULT_SPEED);
		this.ten_meters = ten_meters;
	}
	
	public boolean setSpeedLimit(SpeedLimit limit) {
		if (this.speedLimit != null)
			this.speedLimit.edges.remove(this);
		
		this.speedLimit = limit;
		return limit.edges.add(this);
	}
	
	public SpeedLimit getSpeedLimit() {
		return speedLimit;
	}
	
	@Override
	public String toString() {
		return "Edge [id=" + id + ", originId=" + origin.id + ", destId=" + dest.id + "]";
	}
	
	
}
