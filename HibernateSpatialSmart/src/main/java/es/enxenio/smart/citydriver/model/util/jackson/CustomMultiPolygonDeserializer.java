package es.enxenio.smart.citydriver.model.util.jackson;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

public class CustomMultiPolygonDeserializer extends CustomGeometryDeserializer<MultiPolygon> {

	@Override
	protected MultiPolygon convertGeometry(Geometry geom) {
		logger.debug("Geometry type: {} to MultiPolygon", geom.getGeometryType());

		MultiPolygon ret = null;

		switch (geom.getGeometryType()) {
		case "Polygon":
			Polygon p = (Polygon) geom;
			ret = geometryFactory.createMultiPolygon(new Polygon[] { p });
			break;
		case "MultiPolygon":
			ret = (MultiPolygon) geom;
			break;
		default:
			break;
		}

		return ret;
	}

}
