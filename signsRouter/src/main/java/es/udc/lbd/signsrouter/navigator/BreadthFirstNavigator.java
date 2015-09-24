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
import es.udc.lbd.signsrouter.model.TrafficSign;
import es.udc.lbd.signsrouter.model.TurnRestriction;

public class BreadthFirstNavigator implements Navigator {

	private static final Logger log = Logger.getLogger(BreadthFirstNavigator.class);
	
	private static final int MAX_ITERATIONS = 1000;
	
	private Queue<Edge> frontier;
	private Connection connection;
	private SignDetector detector;
	
	public BreadthFirstNavigator(Connection connection, SignDetector detector) {
		this.frontier = new LinkedList<Edge>();
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
		return 90 - Math.toDegrees(Math.atan2(p2.y - p1.y, p2.x - p1.x));
	}
	
	private void expand(Graph graph, Edge lastEdge) {
		Edge e = null;
		Node node = lastEdge.dest;
		Iterator<Edge> edges = node.outgouingEdges.iterator();
		Set<TrafficSign> detectedSignsForward;
		Set<TrafficSign> detectedSigns;
		double forwardHeading = calculateHeading(lastEdge.posDest, node.position);
		double turnHeading;
		
		// First detect 10 meters away from the end node:
		// Here I'm looking for signs directly forward from my position
		detectedSignsForward = detector.detect(lastEdge.posDest, normalizeAngle(forwardHeading));
		
		if (!detectedSignsForward.isEmpty()) {
			log.debug("From " + lastEdge + " heading forward I can see the following signs: ");
			
			for (TrafficSign sign : detectedSignsForward) {
				log.debug("\t" + sign);
			}
		}
		
		while (edges.hasNext()) {
			e = edges.next();
			
			if (lastEdge != null && e.dest.equals(lastEdge.origin)) {
				continue;
			}
			
			turnHeading = calculateHeading(lastEdge.posDest, e.posOrigin);
			
			for (TrafficSign sign : detectedSignsForward) {
				// FIXME may fail in ambiguous cases
				if (sign.turnRestriction(turnHeading - forwardHeading)) {
					log.debug("New turn restriction from " + lastEdge + " to " + e);
					graph.turnRestrictions.add(new TurnRestriction(lastEdge, e));
				}
			}
			
			if (!e.seen) {
				e.seen = true;
				
				// Then detect from the node itself:
				// Here I'm looking for signs on the direction of the next edge
				detectedSigns = detector.detect(lastEdge.dest.position, normalizeAngle(calculateHeading(lastEdge.dest.position, e.posOrigin)));
				
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
				}
				
				if (!e.banned) {
					frontier.add(e);
				}
			}
			
//			graph.edges.remove(e);
		}
	}
	
	public void navigate(Graph graph, Edge e) {
		long navigatedEdges = 0;
		int maxFrontierSize = 0;
		e.seen = true;
		
		do {
			if (navigatedEdges >= MAX_ITERATIONS) {
				break;
			}
			
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
	}

}
