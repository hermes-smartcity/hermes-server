package es.udc.lbd.signsrouter.detector;

import java.util.Collection;

import es.udc.lbd.signsrouter.model.TrafficSign;

public interface Filter {

	public void filterSigns(Collection<TrafficSign> signs);
}
