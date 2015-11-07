package es.udc.lbd.hermes.model.util.jackson;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

public class CustomMultiLineStringDeserializer extends CustomGeometryDeserializer<MultiLineString> {

	@Override
	protected MultiLineString convertGeometry(Geometry geom) {
		logger.debug("Geometry type: {} to MultiLineString", geom.getGeometryType());

		MultiLineString ret = null;

		switch (geom.getGeometryType()) {
		case "LineString":
			LineString p = (LineString) geom;
			ret = geometryFactory.createMultiLineString(new LineString[] { p });
			break;
		case "MultiLineString":
			ret = (MultiLineString) geom;
			break;
		default:
			break;
		}

		return ret;
	}

}
