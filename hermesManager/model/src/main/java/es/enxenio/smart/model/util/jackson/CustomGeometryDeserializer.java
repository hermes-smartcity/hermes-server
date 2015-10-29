package es.enxenio.smart.model.util.jackson;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.geotools.geojson.geom.GeometryJSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

public abstract class CustomGeometryDeserializer<T extends Geometry> extends JsonDeserializer<T> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public static int SRID = 4326;
	protected static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(
		PrecisionModel.FLOATING), SRID);
	private static GeometryJSON gjson = new GeometryJSON();

	protected abstract T convertGeometry(Geometry geom);

	public T deserialize(String geoJson) throws IOException {

		Reader reader = new StringReader(geoJson);
		Geometry geom = gjson.read(reader);
		// FIXME ver qué se hace con esto, pero si no se indica casca y el GeometryJSON no trabaja con srid ni crs ni nada
		geom.setSRID(SRID);
		return convertGeometry(geom);
	}

	@Override
	public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

		return deserialize(p.readValueAsTree().toString());
	}
}
