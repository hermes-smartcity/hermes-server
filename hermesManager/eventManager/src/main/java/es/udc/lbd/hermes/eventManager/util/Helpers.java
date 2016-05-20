package es.udc.lbd.hermes.eventManager.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.eventManager.json.RoadSectionPoint;
import es.udc.lbd.hermes.model.util.HelpersModel;

public class Helpers {
	
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
		ruta = HelpersModel.wktToGeometry(rutaStr);
		return ruta;
	}
	
	public static Double[] prepararPrecision(List<RoadSectionPoint> roadSection){
		List<Double> precision = new ArrayList<Double>(); 
		for(int i=0;i<roadSection.size();i++){
			precision.add(roadSection.get(i).getAccuracy());
		}
		Double result[] = new Double[precision.size()];
		return precision.toArray(result);
	}

	public static Double[] prepararVelocidad(List<RoadSectionPoint> roadSection){
		List<Double> velocidad = new ArrayList<Double>(); 
		for(int i=0;i<roadSection.size();i++){
			velocidad.add(roadSection.get(i).getSpeed());
		}
		Double result[] = new Double[velocidad.size()];
		return velocidad.toArray(result);
	}
	
	public static Calendar getFecha(String fecha){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date parsedEndDate;
		try {
			parsedEndDate = sdf.parse(fecha);
			Calendar fin = Calendar.getInstance();
			fin.setTime(parsedEndDate);
			return fin;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static String calendarToString(Calendar calendar, String formato){
		String strdate = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		strdate = sdf.format(calendar.getTime());
		
		return strdate;
	}
	
	public static Locale construirLocale(String lang){
		Locale locale = null;
		
		switch (lang) {
		case "es":
			locale = new Locale("es", "ES");
			break;
		case "en":
			locale = Locale.ENGLISH;
			break;
		default:
			locale = Locale.ENGLISH;
			break;
		}
		
		return locale;
	}
}
