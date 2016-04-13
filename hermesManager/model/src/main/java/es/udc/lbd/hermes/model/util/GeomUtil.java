package es.udc.lbd.hermes.model.util;

import com.vividsolutions.jts.geom.Geometry;

public class GeomUtil {

	public static int computeSignificantDecimals(Geometry bounds) {
		
		double width = bounds.getEnvelopeInternal().getWidth();
		if (width > 1) {
			return 2;			
		} else if (width > 0.1) {
			return 3;			
		} else if (width > 0.001) {
			return 4;			
		} else if (width < 0.0001) {
			return 5;			
		} else if (width < 0.00001) {
			return 6;			
		} else if (width < 0.000001) {
			return 7;			
		} else {
			return 8;
		}
	}
}
