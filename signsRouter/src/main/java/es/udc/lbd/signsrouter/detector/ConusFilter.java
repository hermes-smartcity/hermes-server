package es.udc.lbd.signsrouter.detector;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.vividsolutions.jts.geom.Coordinate;

import es.udc.lbd.signsrouter.model.TrafficSign;
import es.udc.lbd.signsrouter.utils.AngleUtils;

public class ConusFilter implements Filter {
	
	private static final Logger log = Logger.getLogger(ConusFilter.class);
	
	private Coordinate position;
	private double heading;
	private float leftAngle;
	private float rightAngle;

	public ConusFilter(Coordinate position, double heading, float leftAngle, float rightAngle) {
		this.position = position;
		this.heading = heading;
		this.leftAngle = leftAngle;
		this.rightAngle = rightAngle;
	}

	public void filterSigns(Collection<TrafficSign> signs) {
		Iterator<TrafficSign> it = signs.iterator();
		double relativeAngle;
		
		while (it.hasNext()) {
			relativeAngle = AngleUtils.diffDegrees(AngleUtils.calculateHeading(position, it.next().position), heading);
			
//			log.debug(relativeAngle);
			
			if (relativeAngle < -leftAngle || relativeAngle > rightAngle)
				it.remove();
		}
	}

}
