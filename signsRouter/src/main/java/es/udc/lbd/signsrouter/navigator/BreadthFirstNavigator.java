package es.udc.lbd.signsrouter.navigator;

import java.util.Iterator;
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
	
	private static final int MAX_ITERATIONS = 3000;
	
	private Queue<Edge> frontier;
	private SignDetector turnDetector;
	private SignDetector forwardDetector;
	
	public BreadthFirstNavigator(SignDetector forwardDetector, SignDetector turnDetector) {
		this.frontier = new LinkedList<Edge>();
		this.turnDetector = turnDetector;
		this.forwardDetector = forwardDetector;
	}
	
	/*
	 * This algorithm should be better:
	 * - One sign can apply to only one edge (Pending)
	 * - Detect all signs first. Could be done with just distance. (Almost done)
	 * - Apply a likehood score for each (sign, edge) pair. Probably based on distance and orientation (Pending)
	 * - Take only the most likely pairs for each sign, above a threshold (Pending)
	 * - Maybe ignore the entire conus detector
	 */
	
	private void expand(Graph graph, Edge lastEdge) {
		SpeedLimit speedLimit = lastEdge.getSpeedLimit();
		Node node = (Node) graph.find(lastEdge.getEdge().getCoordinate(3));	// lastEdge destination
		Coordinate almostDest = lastEdge.getEdge().getCoordinate(2);		// 10m from lastEdge destination
		Set<TrafficSign> detectedSignsForward;
		Set<TrafficSign> detectedSignsTurn;
		double forwardHeading = AngleUtils.calculateHeading(almostDest, node.getCoordinate());
		double turnHeading;
		TurnRestriction turnRestriction = null;
		Iterator<TrafficSign> signsIterator = null;
		TrafficSign sign = null;
		
		// Here I'm looking for signs directly forward from my position
		detectedSignsForward = forwardDetector.detect(lastEdge);
		
		if (!detectedSignsForward.isEmpty()) {
			log.debug("From " + lastEdge + " heading forward I can see the following signs: ");
			
			for (TrafficSign s : detectedSignsForward) {
				log.debug("\t" + s);
			}
		}
		
		for (signsIterator = detectedSignsForward.iterator(); signsIterator.hasNext();) {
			sign = signsIterator.next();
			
			if (sign.speedLimit() > 0) {
				speedLimit = new SpeedLimit(sign.speedLimit());
				
				if (speedLimit.compareTo(lastEdge.getSpeedLimit()) == 0) {
					speedLimit = lastEdge.getSpeedLimit();
					signsIterator.remove();
				} else if (speedLimit.compareTo(lastEdge.getSpeedLimit()) < 0) {
					// Change current speed limit if it's more restrictive
					log.debug("New speed limit of " + sign.speedLimit());
					graph.addSpeedLimit(lastEdge.getSpeedLimit());
					lastEdge.setSpeedLimit(speedLimit);
					graph.addSpeedLimit(speedLimit);
					signsIterator.remove();
				} else {
					
				}
			}
		}
		
		for (signsIterator = detectedSignsForward.iterator(); signsIterator.hasNext();) {
			sign = signsIterator.next();
			
			if (sign.interruptsSpeed()) {
				log.debug("Interrupt speed limit at " + lastEdge);
				graph.addSpeedLimit(lastEdge.getSpeedLimit());
				speedLimit = SpeedLimit.DEFAULT_SPEED;
				signsIterator.remove();
			}
		}
		
		for (Edge e : node.getOutgoingEdges()) {
			
			if (lastEdge.id == -e.id) {
				continue;
			}
			
//			log.debug("From " + lastEdge + " I can go to " + e);
			
			turnRestriction = null;
			Coordinate almostOrigin = e.getEdge().getCoordinate(1);
			turnHeading = AngleUtils.calculateHeading(almostDest, almostOrigin);
			
			for (signsIterator = detectedSignsForward.iterator(); signsIterator.hasNext();) {
				sign = signsIterator.next();
				
				// FIXME may fail in ambiguous cases.
				if (sign.turnRestriction(AngleUtils.diffDegrees(turnHeading, forwardHeading))) {
					log.debug("New turn restriction from " + lastEdge + " to " + e);
					turnRestriction = new TurnRestriction(lastEdge, e);
					graph.turnRestrictions.add(turnRestriction);
					signsIterator.remove();
				}
			}
			
			if (!e.isVisited() || speedLimit.compareTo(e.getSpeedLimit()) < 0) {
				e.setVisited(true);
				
				// Then detect from the node itself:
				// Here I'm looking for signs on the direction of the next edge
				detectedSignsTurn = turnDetector.detect(node.getCoordinate(), AngleUtils.calculateHeading(node.getCoordinate(), almostOrigin), 20f, 15f, 45f);
				
				if (!detectedSignsTurn.isEmpty()) {
					log.debug("From " + lastEdge.dest + " heading to " + e + " I can see the following signs: ");
					
					for (TrafficSign s : detectedSignsTurn) {
						log.debug("\t" + s);
					}
				}
				
				for (signsIterator = detectedSignsTurn.iterator(); signsIterator.hasNext();) {
					sign = signsIterator.next();
					
					if (sign.noWay()) {
						log.debug("Banning edge " + e);
						graph.banEdge(e);
					}
				}
				
				if (!e.banned && turnRestriction == null) {
					e.setSpeedLimit(speedLimit);
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
