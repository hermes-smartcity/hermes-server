package es.udc.lbd.signsrouter.model;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geomgraph.DirectedEdgeStar;
import com.vividsolutions.jts.operation.overlay.OverlayNodeFactory;

public class RouterNodeFactory extends OverlayNodeFactory {
	@Override
	public Node createNode(Coordinate coord) {
	    return new Node(null, coord, new DirectedEdgeStar());
	  }
}
