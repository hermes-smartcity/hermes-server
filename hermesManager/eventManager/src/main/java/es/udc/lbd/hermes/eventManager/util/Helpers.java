package es.udc.lbd.hermes.eventManager.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import es.udc.lbd.hermes.eventManager.json.RoadSectionPoint;

public class Helpers {
	public static Geometry wktToGeometry(String wktPoint) {
		WKTReader fromText = new WKTReader();
		Geometry geom = null;
		try {
			geom = fromText.read(wktPoint);
			// TODO cambiado porque en la BD está asignado ese SRID. Habrá que decidir cual va a ser y ponerlo según eso
			geom.setSRID(4326);
		} catch (ParseException e) {
			throw new RuntimeException("Not a WKT string:" + wktPoint);
		}
		return geom;
	}

	/**
	 * Utility method to assemble all arguments save the first into a String
	 */
	public static String assemble(String[] args) {
		StringBuilder builder = new StringBuilder();
		for (int i = 1; i < args.length; i++) {
			builder.append(args[i]).append(" ");
		}
		return builder.toString();
	}

	public static Calendar getFecha(String fecha) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			cal.setTime(sdf.parse(fecha));
		} catch (java.text.ParseException e) {			
			// TODO Añadirlo al log
			e.printStackTrace();
		}

		return cal;
	}
	
	public static Geometry prepararPunto(Double latitude, Double longitude){
		return Helpers.wktToGeometry("POINT("+ longitude.toString() + " "+ latitude.toString() + ")");
	}
	
	public static Geometry prepararRuta(List<RoadSectionPoint> roadSection){
		Geometry ruta = null; 
		// Contruimos el array de puntos con linestring, primero el string con cada uno de los puntos (coordenadas latitud y longitud) que sacamos del objeto json
		String rutaStr="LINESTRING(";
		for(int i=0;i<roadSection.size();i++){
			RoadSectionPoint puntoRuta = roadSection.get(i);
			rutaStr+=puntoRuta.getLongitude() + " " + puntoRuta.getLatitude() +" , ";
		}
		rutaStr = rutaStr.substring(0,rutaStr.length()-3)+")";
		
		// Lo convertimos a GIS
		ruta = Helpers.wktToGeometry(rutaStr);
		return ruta;
	}
}
