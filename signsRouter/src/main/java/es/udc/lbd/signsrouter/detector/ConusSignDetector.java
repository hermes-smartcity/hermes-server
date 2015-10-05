package es.udc.lbd.signsrouter.detector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.strtree.STRtree;

import es.udc.lbd.signsrouter.model.TrafficSign;

public class ConusSignDetector implements SignDetector {

	private static final Logger log = Logger.getLogger(ConusSignDetector.class);
	
	private SpatialIndex index;
	private Envelope envelope;
	
	public ConusSignDetector(Set<TrafficSign> signs) {
		STRtree tree = new STRtree();
		this.index = tree;
		
		for (TrafficSign sign : signs) {
			tree.insert(new Envelope(sign.position), sign);
		}
		
		tree.build();
		log.info("Built a sign index with " + tree.size() + " items.");
		
		this.envelope = new Envelope();
	}

	public Set<TrafficSign> detect(final Object... parameters) {
		final Coordinate p = (Coordinate) parameters[0];
		final double heading = (Double) parameters[1];
		final float radius = (Float) parameters[2];
		final float leftAngle = (Float) parameters[3];
		final float rightAngle = (Float) parameters[4];
		
		Set<TrafficSign> signs = new HashSet<TrafficSign>();
		List<Filter> filters = new ArrayList<Filter>(3) {{
			add(new RadiusFilter(p, radius));
			add(new ConusFilter(p, heading, leftAngle, rightAngle));
			add(new LookAtMeFilter(p));
		}};
		
//		log.debug("Searching for signs from " + p + " heading to " + heading);
		
		envelope.init(p.x-radius, p.x+radius, p.y-radius, p.y+radius);
		signs.addAll(index.query(envelope));
		
		for (Filter filter : filters) {
			filter.filterSigns(signs);
		}
		
		return signs;
	}

}
