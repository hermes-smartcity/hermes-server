package es.udc.lbd.signsrouter.detector;

import java.util.Set;

import es.udc.lbd.signsrouter.model.TrafficSign;

public interface SignDetector {
	public Set<TrafficSign> detect(final Object... parameters);
}
