package es.udc.lbd.signsrouter.navigator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import es.udc.lbd.signsrouter.model.Edge;
import es.udc.lbd.signsrouter.model.TrafficSign;

public class DetectedSigns {
	
	private static final Logger log = Logger.getLogger(DetectedSigns.class);
	
	private Map<TrafficSign, List<DetectedSign>> detectedSigns = new HashMap<TrafficSign, List<DetectedSign>>();
	
	public boolean put(TrafficSign sign, Edge e, double d) {
		if (!detectedSigns.containsKey(sign))
			detectedSigns.put(sign, new ArrayList<DetectedSign>());
		
		detectedSigns.get(sign).add(new DetectedSign(sign, e, (float) d));
		
		if (detectedSigns.get(sign).size() > 1) {
			log.debug("There is a sign with more than one possible edge: " + sign);
		}
		
		return true;
	}
	
	public Map<Edge, List<TrafficSign>> getSignsByEdge() {
		Map<Edge, List<TrafficSign>> map = new HashMap<Edge, List<TrafficSign>>();
		
		for (TrafficSign sign : detectedSigns.keySet()) {
			Collections.sort(detectedSigns.get(sign));
			DetectedSign d = detectedSigns.get(sign).get(0);
			
			if (! map.containsKey(d.edge)) {
				map.put(d.edge, new ArrayList<TrafficSign>());
			}
			
			map.get(d.edge).add(sign);
		}
		
		return map;
	}
}
