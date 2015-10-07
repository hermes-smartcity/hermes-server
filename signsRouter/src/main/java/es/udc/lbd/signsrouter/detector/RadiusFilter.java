package es.udc.lbd.signsrouter.detector;

import java.util.Collection;
import java.util.Iterator;

import com.vividsolutions.jts.geom.Coordinate;

import es.udc.lbd.signsrouter.model.TrafficSign;

public class RadiusFilter implements Filter {
	
	private Coordinate position;
	private float radius;

	public RadiusFilter(Coordinate position, float radius) {
		this.position = position;
		this.radius = radius;
	}

	public void filterSigns(Collection<TrafficSign> signs) {
		Iterator<TrafficSign> it = signs.iterator();
		
		while (it.hasNext()) {
			if (it.next().position.distance(position) > radius)
				it.remove();
		}
	}
}
