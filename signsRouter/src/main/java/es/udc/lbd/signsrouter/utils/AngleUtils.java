package es.udc.lbd.signsrouter.utils;

import com.vividsolutions.jts.algorithm.Angle;
import com.vividsolutions.jts.geom.Coordinate;

public class AngleUtils extends Angle {
	public static double calculateHeading(Coordinate p1, Coordinate p2) {
		return Angle.toDegrees(Angle.normalize(Angle.PI_OVER_2 - Angle.angle(p1, p2)));
	}
	
	public static double diffDegrees(double ang1, double ang2) {
		return Angle.toDegrees(Angle.normalize(Angle.toRadians(ang1) - Angle.toRadians(ang2)));
	}
}
