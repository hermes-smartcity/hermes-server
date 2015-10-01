package es.udc.lbd.signsrouter.model;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geomgraph.EdgeEndStar;

public class Node extends com.vividsolutions.jts.geomgraph.Node {

	private static final long serialVersionUID = 1L;
	
	public Long id;
	
	public Node(Long id, Coordinate coord, EdgeEndStar edges) {
		super(coord, edges);
		this.id = id;
	}
	
	public List<Edge> getOutgoingEdges() {
		return this.edges.getEdges();
	}
	
	@Override
	public String toString() {
		return "" + id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Node other = (Node) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
