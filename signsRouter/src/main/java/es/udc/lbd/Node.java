package es.udc.lbd;

import java.util.HashSet;
import java.util.Set;

public class Node {
	public Long id;
	public Position position;
	public transient Set<Edge> incomingEdges;
	public transient Set<Edge> outgouingEdges;
	
	public Node(Long id, Position pos) {
		super();
		this.id = id;
		this.position = pos;
		this.incomingEdges = new HashSet<Edge>(); 
		this.outgouingEdges = new HashSet<Edge>(); 
	}
	
	public boolean addIncomingEdge(Edge e) {
		return this.incomingEdges.add(e);
	}
	
	public boolean addOutgoingEdge(Edge e) {
		return this.outgouingEdges.add(e);
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
