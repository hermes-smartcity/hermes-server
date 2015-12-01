package es.udc.lbd.hermes.model.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;


public class HelpersModel {
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
	
	//Inicio del día, para comparaciones
	public static Calendar getLimiteDiaInferior(Calendar fecha2) {
		Calendar fecha=Calendar.getInstance();
		fecha.setTimeInMillis(fecha2.getTimeInMillis());
		fecha.set(Calendar.HOUR_OF_DAY, 0);
		fecha.set(Calendar.MINUTE,0);
		fecha.set(Calendar.SECOND,0);
		fecha.set(Calendar.MILLISECOND,0);
		return fecha;
	}
		
	public static Calendar getHoy() {
		return getLimiteDiaInferior(Calendar.getInstance());
	}
		
	public static Calendar getAyer(Calendar fechaHoy){
		Calendar fecha=Calendar.getInstance();
		fecha.setTimeInMillis(fechaHoy.getTimeInMillis());
		fecha.add(Calendar.HOUR,-24);
		return getLimiteDiaInferior(fecha);
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
		return wktToGeometry("POINT("+ longitude.toString() + " "+ latitude.toString() + ")");
	}
	
	public static Geometry prepararPoligono(Double wnLng, Double wnLat, Double esLng, Double esLat){
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );

		WKTReader reader = new WKTReader( geometryFactory );
		Polygon polygon = null;
		try {
			String str = "POLYGON(("+wnLng+" "+ wnLat +" , "+
					+ wnLng+" " +esLat+" , "+  esLng +" "+esLat+" , "+  esLng +" "+wnLat + " , " +wnLng+" "+ wnLat +"))";

			polygon = (Polygon) reader.read(str);
			
			polygon.setSRID(4326);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			return polygon;
	}
	
}
