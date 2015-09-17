package es.udc.lbd;

import java.util.Set;

public interface SignDetector {
	public Set<TrafficSign> detect(Position p, double d);
}
