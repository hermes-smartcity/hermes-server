package es.udc.lbd.signsrouter.navigator;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.apache.log4j.Logger;

import com.vividsolutions.jts.geom.Coordinate;

import es.udc.lbd.signsrouter.detector.SignDetector;
import es.udc.lbd.signsrouter.model.Edge;
import es.udc.lbd.signsrouter.model.Graph;
import es.udc.lbd.signsrouter.model.Node;
import es.udc.lbd.signsrouter.model.SpeedLimit;
import es.udc.lbd.signsrouter.model.TrafficSign;
import es.udc.lbd.signsrouter.model.TurnRestriction;
import es.udc.lbd.signsrouter.utils.AngleUtils;

public class BreadthFirstNavigator implements Navigator {

	private static final Logger log = Logger.getLogger(BreadthFirstNavigator.class);
	
	private static final int MAX_ITERATIONS = 10;
	
	private Queue<Edge> frontier;
	private SignDetector detector;
	
	public BreadthFirstNavigator(SignDetector detector) {
		this.frontier = new LinkedList<Edge>();
		this.detector = detector;
	}
	
	private void expand(Graph graph, Edge lastEdge) {
		SpeedLimit speedLimit = lastEdge.getSpeedLimit();
		SpeedLimit newSpeedLimit = speedLimit;
		Node node = (Node) graph.find(lastEdge.getEdge().getCoordinate(3));	// lastEdge destination
		Coordinate almostDest = lastEdge.getEdge().getCoordinate(2);		// 10m from lastEdge destination
		Set<TrafficSign> detectedSignsForward;
		Set<TrafficSign> detectedSignsTurn;
		double forwardHeading = AngleUtils.calculateHeading(almostDest, node.getCoordinate());
		double turnHeading;
		TurnRestriction turnRestriction = null;
		
		// First detect 10 meters away from the end node:
		// Here I'm looking for signs directly forward from my position
		detectedSignsForward = detector.detect(almostDest, forwardHeading, 10, 15, 60);
		
		if (!detectedSignsForward.isEmpty()) {
			log.debug("From " + lastEdge + " heading forward I can see the following signs: ");
			
			for (TrafficSign sign : detectedSignsForward) {
				log.debug("\t" + sign);
			}
		}
		
		for (TrafficSign sign: detectedSignsForward) {
			if (sign.interruptsSpeed()) {
				log.debug("Interrupt speed limit at " + lastEdge);
				graph.addSpeedLimit(lastEdge.getSpeedLimit());
				speedLimit = SpeedLimit.DEFAULT_SPEED;
			}
		}
		
		for (Edge e : node.getOutgoingEdges()) {
			
			if (lastEdge.id == -e.id) {
				continue;
			}
			
//			log.debug("From " + lastEdge + " I can go to " + e);
			
			newSpeedLimit = speedLimit;
			turnRestriction = null;
			Coordinate almostOrigin = e.getEdge().getCoordinate(1);
			turnHeading = AngleUtils.calculateHeading(almostDest, almostOrigin);
			
			for (TrafficSign sign : detectedSignsForward) {
				// FIXME may fail in ambiguous cases.
				if (sign.turnRestriction(AngleUtils.diffDegrees(turnHeading, forwardHeading))) {
					log.debug("New turn restriction from " + lastEdge + " to " + e);
					turnRestriction = new TurnRestriction(lastEdge, e);
					graph.turnRestrictions.add(turnRestriction);
				}
			}
			
			if (!e.isVisited() || speedLimit.compareTo(e.getSpeedLimit()) > 0) {
				e.setVisited(true);
				
				// Then detect from the node itself:
				// Here I'm looking for signs on the direction of the next edge
				detectedSignsTurn = detector.detect(node.getCoordinate(), AngleUtils.calculateHeading(node.getCoordinate(), almostOrigin), 20, 15, 60);
				
				if (!detectedSignsTurn.isEmpty()) {
					log.debug("From " + lastEdge.dest + " heading to " + e + " I can see the following signs: ");
					
					for (TrafficSign sign : detectedSignsTurn) {
						log.debug("\t" + sign);
					}
				}
				
				for (TrafficSign sign : detectedSignsTurn) {
					if (sign.noWay()) {
						log.debug("Banning edge " + e);
						graph.banEdge(e);
					}
					
					if (sign.speedLimit() > 0) {
						newSpeedLimit = new SpeedLimit(sign.speedLimit());
						
						if (newSpeedLimit.compareTo(e.getSpeedLimit()) == 0) {
							newSpeedLimit = e.getSpeedLimit();
						} else if (newSpeedLimit.compareTo(speedLimit) < 0) {
							log.debug("New speed limit of " + sign.speedLimit());
							graph.addSpeedLimit(newSpeedLimit);
						} else {
							newSpeedLimit = speedLimit;
						}
					}
				}
				
				if (!e.banned && turnRestriction == null) {
					e.setSpeedLimit(newSpeedLimit);
					frontier.add(e);
				}
			}
		}
	}
	
	public void navigate(Graph graph, Edge e) {
		long navigatedEdges = 0;
		int maxFrontierSize = 0;
		e.setVisited(true);
		
		do {
//			if (navigatedEdges >= MAX_ITERATIONS) {
//				break;
//			}
			
			if (! frontier.isEmpty()) {
				e = frontier.remove();
				
//				log.debug("Navigating " + e);
				navigatedEdges++;
				maxFrontierSize = Math.max(maxFrontierSize, frontier.size());
			}
			
			expand(graph, e);
			
		} while (! frontier.isEmpty());
		
		log.info("Navigated " + navigatedEdges + " edges.");
		log.debug("Max frontier size: " + maxFrontierSize + " edges.");
		log.info("Banned " + graph.bannedEdges.size() + " edges.");
		log.info(graph.turnRestrictions.size() + " turn restricions.");
		log.info(graph.speedLimits.size() + " speed limits.");
		
		log.debug("Speed limits: ");
		for (SpeedLimit limit : graph.speedLimits) {
			log.debug(limit.speed + ": " + limit.edges.size() + " edges.");
			
			if (limit != SpeedLimit.DEFAULT_SPEED)
				log.debug(limit);
				
		}
	}

}
