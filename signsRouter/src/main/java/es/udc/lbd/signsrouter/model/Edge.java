package es.udc.lbd.signsrouter.model;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Location;
import com.vividsolutions.jts.geomgraph.Label;

public class Edge extends com.vividsolutions.jts.geomgraph.DirectedEdge {
	public Long id;
	public Node origin;
	public Node dest;
	public boolean banned = false;
	
	public Edge(Long id, Node origin, Node dest, Coordinate posOrigin, Coordinate posDest) {
		super(
				new com.vividsolutions.jts.geomgraph.Edge(
						new Coordinate[] {origin.getCoordinate(), posOrigin, posDest, dest.getCoordinate()}, new Label(Location.NONE)), true);
		
		if (id > 0)
			super.setSym(new Edge(-id, dest, origin, posDest, posOrigin));
		
		this.id = id;
		this.origin = origin;
		this.dest = dest;
	} 

	@Override
	public String toString() {
		return "Edge [id=" + id + ", originId=" + origin.id + ", destId=" + dest.id + "]";
	}
	
	
}
