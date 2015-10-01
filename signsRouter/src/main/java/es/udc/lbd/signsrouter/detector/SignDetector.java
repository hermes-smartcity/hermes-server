package es.udc.lbd.signsrouter.detector;

import java.util.Set;

import com.vividsolutions.jts.geom.Coordinate;

import es.udc.lbd.signsrouter.model.TrafficSign;

public interface SignDetector {
	public Set<TrafficSign> detect(Coordinate p, double heading, float radius, float leftAngle, float rightAngle);
}
