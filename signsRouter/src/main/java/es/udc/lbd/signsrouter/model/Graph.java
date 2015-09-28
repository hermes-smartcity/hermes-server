package es.udc.lbd.signsrouter.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {
	public Map<Long, Edge> edges;
	public Node[] nodes;
	public Set<Edge> bannedEdges = new HashSet<Edge>();;
	public Set<TurnRestriction> turnRestrictions = new HashSet<TurnRestriction>();
	public Set<SpeedLimit> speedLimits = new HashSet<SpeedLimit>();
	public int epsg = 4326;
	
	public Graph(int nodes) {
		this.edges = new HashMap<Long, Edge>(1000);
		this.nodes = new Node[nodes];
	}
	
	public Graph(int edges, int nodes) {
		this.edges = new HashMap<Long, Edge>(edges);
		this.nodes = new Node[nodes];
	}
	
	public boolean addEdge(Edge e) {
		if (nodes[e.origin.id.intValue()] == null)
			nodes[e.origin.id.intValue()] = e.origin;
		if (nodes[e.dest.id.intValue()] == null)
			nodes[e.dest.id.intValue()] = e.dest;
		
		e.origin = nodes[e.origin.id.intValue()];
		e.dest = nodes[e.dest.id.intValue()];
		e.origin.addOutgoingEdge(e);
//		e.dest.addIncomingEdge(e);
		
		return this.edges.put(e.id, e) != null;
	}
	
	public boolean banEdge(Edge e) {
		e.banned = true;
		return this.bannedEdges.add(e);
	}
	
	public boolean addSpeedLimit(SpeedLimit speedLimit) {
		if (speedLimit == null)
			return false;
		
		return this.speedLimits.add(speedLimit);
	}
}
