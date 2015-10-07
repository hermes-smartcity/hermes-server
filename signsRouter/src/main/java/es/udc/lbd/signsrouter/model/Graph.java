package es.udc.lbd.signsrouter.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.vividsolutions.jts.geomgraph.GeometryGraph;
import com.vividsolutions.jts.geomgraph.NodeMap;

public class Graph extends GeometryGraph {
	
	private Map<Long, Edge> edges;
//	public Map<Long, Node> nodes;
	public Set<Edge> bannedEdges = new HashSet<Edge>();;
	public Set<TurnRestriction> turnRestrictions = new HashSet<TurnRestriction>();
	public Set<SpeedLimit> speedLimits = new HashSet<SpeedLimit>();
	public int epsg = 4326;
	
	public Graph() {
		super(0, null);
		super.nodes = new NodeMap(new RouterNodeFactory());
		this.edges = new HashMap<Long, Edge>();
//		this.nodes = new HashMap<Long, Node>();
	}
	
	public void add(Edge e) {
		super.add(e);
		this.edges.put(e.id, e);
		((Node) e.getNode()).id = e.origin.id;
		e.origin = ((Node) e.getNode());
	}
	
	public Edge findEdge(Long id) {
		return this.edges.get(id);
	}
	
//	public boolean addEdge(Edge e) {
//		if (nodes[e.origin.id.intValue()] == null)
//			nodes[e.origin.id.intValue()] = e.origin;
//		if (nodes[e.dest.id.intValue()] == null)
//			nodes[e.dest.id.intValue()] = e.dest;
//		
//		e.origin = nodes[e.origin.id.intValue()];
//		e.dest = nodes[e.dest.id.intValue()];
//		e.origin.addOutgoingEdge(e);
////		e.dest.addIncomingEdge(e);
//		
//		return this.edges.put(e.id, e) != null;
//	}
	
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
