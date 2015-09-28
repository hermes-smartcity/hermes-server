package es.udc.lbd.signsrouter.navigator;

import java.sql.Connection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.apache.log4j.Logger;

import es.udc.lbd.signsrouter.detector.SignDetector;
import es.udc.lbd.signsrouter.model.Edge;
import es.udc.lbd.signsrouter.model.Graph;
import es.udc.lbd.signsrouter.model.Node;
import es.udc.lbd.signsrouter.model.Position;
import es.udc.lbd.signsrouter.model.SpeedLimit;
import es.udc.lbd.signsrouter.model.TrafficSign;
import es.udc.lbd.signsrouter.model.TurnRestriction;

public class BreadthFirstNavigator implements Navigator {

	private static final Logger log = Logger.getLogger(BreadthFirstNavigator.class);
	
	private static final int MAX_ITERATIONS = 1000;
	
	private Queue<NavigatorState> frontier;
	private Connection connection;
	private SignDetector detector;
	
	public BreadthFirstNavigator(Connection connection, SignDetector detector) {
		this.frontier = new LinkedList<NavigatorState>();
		this.connection = connection;
		this.detector = detector;
	}
	
	private static double normalizeAngle(double a) {
		int turns = (int) (a / 360);
		
		if (a < 0)
			turns--;
		
		return a - turns * 360;
	}
	
	private static double calculateHeading(Position p1, Position p2) {
		return normalizeAngle(90 - Math.toDegrees(Math.atan2(p2.y - p1.y, p2.x - p1.x)));
	}
	
	private void expand(Graph graph, NavigatorState state) {
		Edge lastEdge = state.edge;
		SpeedLimit speedLimit = state.speedLimit;
		Edge e = null;
		Node node = lastEdge.dest;
		Iterator<Edge> edges = node.outgouingEdges.iterator();
		Set<TrafficSign> detectedSignsForward;
		Set<TrafficSign> detectedSigns;
		double forwardHeading = calculateHeading(lastEdge.posDest, node.position);
		double turnHeading;
		TurnRestriction turnRestriction = null;
		
		if (speedLimit != null) {
			speedLimit.edges.add(lastEdge);
		}
		
		// First detect 10 meters away from the end node:
		// Here I'm looking for signs directly forward from my position
		detectedSignsForward = detector.detect(lastEdge.posDest, forwardHeading);
		
		if (!detectedSignsForward.isEmpty()) {
			log.debug("From " + lastEdge + " heading forward I can see the following signs: ");
			
			for (TrafficSign sign : detectedSignsForward) {
				log.debug("\t" + sign);
			}
		}
		
		for (TrafficSign sign: detectedSignsForward) {
			if (sign.interruptsSpeed()) {
				log.debug("Interrupt speed limit at " + lastEdge);
				graph.addSpeedLimit(speedLimit);
				speedLimit = null;
			}
		}
		
		while (edges.hasNext()) {
			e = edges.next();
			
			if (lastEdge != null && e.dest.equals(lastEdge.origin)) {
				continue;
			}
			
			turnRestriction = null;
			turnHeading = calculateHeading(lastEdge.posDest, e.posOrigin);
			
			for (TrafficSign sign : detectedSignsForward) {
				// FIXME may fail in ambiguous cases.
				if (sign.turnRestriction(normalizeAngle(turnHeading - forwardHeading))) {
					log.debug("New turn restriction from " + lastEdge + " to " + e);
					turnRestriction = new TurnRestriction(lastEdge, e);
					graph.turnRestrictions.add(turnRestriction);
				}
			}
			
			if (!e.seen) {
				e.seen = true;
				
				// Then detect from the node itself:
				// Here I'm looking for signs on the direction of the next edge
				detectedSigns = detector.detect(lastEdge.dest.position, calculateHeading(lastEdge.dest.position, e.posOrigin));
				
				if (!detectedSigns.isEmpty()) {
					log.debug("From " + lastEdge.dest + " heading to " + e + " I can see the following signs: ");
					
					for (TrafficSign sign : detectedSigns) {
						log.debug("\t" + sign);
					}
				}
				
				for (TrafficSign sign : detectedSigns) {
					if (sign.noWay()) {
						log.debug("Banning edge " + e);
						graph.banEdge(e);
					}
					
					if (sign.speedLimit() > 0) {
						log.debug("New speed limit of " + sign.speedLimit());
						speedLimit = new SpeedLimit(sign.speedLimit());
						graph.addSpeedLimit(speedLimit);
					}
				}
				
				if (!e.banned && turnRestriction == null) {
					frontier.add(new NavigatorState(e, speedLimit));
				}
			}
			
//			graph.edges.remove(e);
		}
	}
	
	public void navigate(Graph graph, Edge e) {
		long navigatedEdges = 0;
		int maxFrontierSize = 0;
		e.seen = true;
		NavigatorState state = new NavigatorState(e, null);
		
		do {
			if (navigatedEdges >= MAX_ITERATIONS) {
				break;
			}
			
			if (! frontier.isEmpty()) {
				state = frontier.remove();
				
//				log.debug("Navigating " + e);
				navigatedEdges++;
				maxFrontierSize = Math.max(maxFrontierSize, frontier.size());
			}
			
			expand(graph, state);
			
		} while (! frontier.isEmpty());
		
		log.info("Navigated " + navigatedEdges + " states.");
		log.debug("Max frontier size: " + maxFrontierSize + " states.");
		log.info("Banned " + graph.bannedEdges.size() + " edges.");
		log.info(graph.turnRestrictions.size() + " turn restricions.");
		log.info(graph.speedLimits.size() + " speed limits.");
		
		log.debug("Speed limits: " + graph.speedLimits);
	}

}
