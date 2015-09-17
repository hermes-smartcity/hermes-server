package es.udc.lbd;

import java.sql.Connection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.apache.log4j.Logger;

public class BreadthFirstNavigator implements Navigator {

	private static final Logger log = Logger.getLogger(BreadthFirstNavigator.class);
	
	private static final int MAX_ITERATIONS = 10;
	
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
	
	private static double calculateHeading(Position p, Edge e) {
		return normalizeAngle(90 - Math.toDegrees(Math.atan2(e.posOrigin.y - p.y, e.posOrigin.x - p.x)));
	}
	
	private void expand(Graph graph, Node origin, Node lastNode) {
		Edge e = null;
		Iterator<Edge> edges = origin.outgouingEdges.iterator();
		Set<TrafficSign> detectedSigns;
			
		while (edges.hasNext()) {
			e = edges.next();
			
			if (e.dest.equals(lastNode)) {
				continue;
			}
			
			edges.remove();	// TODO mark as seen instead of removing it
			
			detectedSigns = detector.detect(origin.position, calculateHeading(origin.position, e));
			
			if (!detectedSigns.isEmpty()) {
				log.debug("From " + origin + " heading to " + e + " I can see the following signs: ");
				
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
			
			if (!e.banned)
				frontier.add(e);
			
//			graph.edges.remove(e);
		}
	}
	
	public void navigate(Graph graph, Node origin) {
		Node last = null;
		Edge e = null;
		
		long navigatedEdges = 0;
		int maxFrontierSize = 0;
		
		do {
			if (navigatedEdges >= MAX_ITERATIONS) {
				break;
			}
			
			if (! frontier.isEmpty()) {
				e = frontier.remove();
				
//				log.debug("Navigating " + e);
				navigatedEdges++;
				maxFrontierSize = Math.max(maxFrontierSize, frontier.size());
				
				origin = e.dest;
				last = e.origin;
			}
			
			expand(graph, origin, last);
		} while (! frontier.isEmpty());
		
		log.info("Navigated " + navigatedEdges + " edges.");
		log.debug("Max frontier size: " + maxFrontierSize + " edges.");
		log.info("Banned " + graph.bannedEdges.size() + " edges.");
	}

}
