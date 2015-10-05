package es.udc.lbd.signsrouter.detector;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.vividsolutions.jts.geom.Coordinate;

import es.udc.lbd.signsrouter.model.TrafficSign;
import es.udc.lbd.signsrouter.utils.AngleUtils;

public class LookAtMeFilter implements Filter {
	
	private static final Logger log = Logger.getLogger(LookAtMeFilter.class);
	
	private Coordinate position;

	public LookAtMeFilter(Coordinate position) {
		this.position = position;
	}

	public Coordinate getPosition() {
		return position;
	}

	public void setPosition(Coordinate position) {
		this.position = position;
	}

	public void filterSigns(Collection<TrafficSign> signs) {
		Iterator<TrafficSign> it = signs.iterator();
		TrafficSign sign;
		
		while (it.hasNext()) {
			sign = it.next();
			
//			log.debug(sign + " at " + AngleUtils.calculateHeading(sign.position, position));
			
			if (Math.abs(AngleUtils.diffDegrees(AngleUtils.calculateHeading(sign.position, position), sign.azimut)) > 80)
				it.remove();
		}
	}

}
