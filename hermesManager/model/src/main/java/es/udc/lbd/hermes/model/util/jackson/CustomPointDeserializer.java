package es.udc.lbd.hermes.model.util.jackson;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

public class CustomPointDeserializer extends CustomGeometryDeserializer<Point> {

	@Override
	protected Point convertGeometry(Geometry geom) {
//		logger.debug("Geometry type: {} to Point", geom.getGeometryType());

		Point ret = null;

		switch (geom.getGeometryType()) {
		case "Point":
			ret = (Point) geom;
			break;
		default:
			break;
		}

		return ret;
	}

}
