package es.udc.lbd.hermes.eventManager.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import es.udc.lbd.hermes.eventManager.json.RoadSectionPoint;
import es.udc.lbd.hermes.eventManager.json.ZtreamyDataSection;
import es.udc.lbd.hermes.eventManager.json.ZtreamyDriverFeatures;
import es.udc.lbd.hermes.eventManager.json.ZtreamyHighAcceleration;
import es.udc.lbd.hermes.eventManager.json.ZtreamyHighDeceleration;
import es.udc.lbd.hermes.eventManager.json.ZtreamyHighHeartRate;
import es.udc.lbd.hermes.eventManager.json.ZtreamyHighSpeed;
import es.udc.lbd.hermes.eventManager.json.ZtreamyVehicleLocation;
import es.udc.lbd.hermes.model.events.EventType;
import es.udc.lbd.hermes.model.events.dataSection.DataSection;
import es.udc.lbd.hermes.model.events.driverFeatures.DriverFeatures;
import es.udc.lbd.hermes.model.events.eventoProcesado.EventoProcesado;
import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;

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

	public static DataSection procesaEvento(ZtreamyDataSection ztreamyDataSection) {
		DataSection dataSection = new DataSection();
		dataSection.setMinSpeed(ztreamyDataSection.getMedianSpeed());
		dataSection.setMaxSpeed(ztreamyDataSection.getMaxSpeed());
		dataSection.setMedianSpeed(ztreamyDataSection.getMedianSpeed());
		dataSection.setAverageSpeed(ztreamyDataSection.getAverageSpeed());
		dataSection.setAverageRR(ztreamyDataSection.getAverageRR());
		dataSection.setAverageHeartRate(ztreamyDataSection.getAverageHeartRate());
		dataSection.setStandardDeviationSpeed(ztreamyDataSection.getStandardDeviationSpeed());
		dataSection.setStandardDeviationRR(ztreamyDataSection.getStandardDeviationRR());
		dataSection.setStandardDeviationHeartRate(ztreamyDataSection.getStandardDeviationHeartRate());
		dataSection.setPke(ztreamyDataSection.getPke());
		// TODO Falta decidir como se va a hacer
		dataSection.setRoadSection((LineString) prepararRuta(ztreamyDataSection.getRoadSection()));
		return dataSection;
			
	}
		
	public static VehicleLocation procesaEvento(ZtreamyVehicleLocation ztreamyVehicleLocation) {
		VehicleLocation vehicleLocation = new VehicleLocation();	
		Geometry punto = prepararPunto(ztreamyVehicleLocation.getLatitude(),ztreamyVehicleLocation.getLongitude());
		vehicleLocation.setPosition((Point)punto);
		return vehicleLocation;
	}
	
	public static Measurement procesaEvento(ZtreamyHighAcceleration ztreamyHighAcceleration){
		Measurement measurement = new Measurement();
		Geometry punto = prepararPunto(ztreamyHighAcceleration.getLatitude(),ztreamyHighAcceleration.getLongitude());
		measurement.setPosition((Point)punto);
		measurement.setValue(ztreamyHighAcceleration.getValue());
		// TODO hacer fuera
//		measurement.setTipo("High Acceleration");
		return measurement;
	}
	
	public static Measurement procesaEvento(ZtreamyHighDeceleration ztreamyHighDeceleration){
		Measurement measurement = new Measurement();
		Geometry punto = prepararPunto(ztreamyHighDeceleration.getLatitude(),ztreamyHighDeceleration.getLongitude());
		measurement.setPosition((Point)punto);
		measurement.setValue(ztreamyHighDeceleration.getValue());
		// TODO hacer fuera
//		measurement.setTipo(High Deceleration");
		return measurement;
	}
	
	public static Measurement procesaEvento(ZtreamyHighHeartRate ztreamyHighHeartRate){
		Measurement measurement = new Measurement();
		Geometry punto = prepararPunto(ztreamyHighHeartRate.getLatitude(),ztreamyHighHeartRate.getLongitude());
		measurement.setPosition((Point)punto);
		measurement.setValue(ztreamyHighHeartRate.getValue());
		// TODO hacer fuera
//		measurement.setTipo(High Heart Rate");
		return measurement;
	}
	
	public static Measurement procesaEvento(ZtreamyHighSpeed ztreamyHighSpeed){
		Measurement measurement = new Measurement();
		Geometry punto = prepararPunto(ztreamyHighSpeed.getLatitude(),ztreamyHighSpeed.getLongitude());
		measurement.setPosition((Point)punto);
		measurement.setValue(ztreamyHighSpeed.getValue());
		// TODO hacer fuera
//		measurement.setTipo(High Speed");
		return measurement;
	}
	
	public static DriverFeatures procesaEvento(ZtreamyDriverFeatures ztreamyDriverFeatures){
		DriverFeatures driverFeatures = new DriverFeatures();
		driverFeatures.setAwakeFor(ztreamyDriverFeatures.getAwakeFor());		
		driverFeatures.setInBed(ztreamyDriverFeatures.getInBed()); 
		driverFeatures.setWorkingTime(ztreamyDriverFeatures.getWorkingTime()); 
		driverFeatures.setLightSleep(ztreamyDriverFeatures.getLightSleep());
		driverFeatures.setDeepSleep(ztreamyDriverFeatures.getDeepSleep());
		driverFeatures.setPreviousStress(ztreamyDriverFeatures.getPreviousStress());
		return driverFeatures;
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
	
	private static Geometry prepararPunto(Double latitude, Double longitude){
		return Helpers.wktToGeometry("POINT("+ latitude.toString() + " "+ longitude.toString() + ")");
	}
	
	private static Geometry prepararRuta(List<RoadSectionPoint> roadSection){
		Geometry ruta = null; 
		// Contruimos el array de puntos con linestring, primero el string con cada uno de los puntos (coordenadas latitud y longitud) que sacamos del objeto json
		String rutaStr="LINESTRING(";
		for(int i=0;i<roadSection.size();i++){
			RoadSectionPoint puntoRuta = roadSection.get(i);
			rutaStr+=puntoRuta.getLatitude() + " " + puntoRuta.getLongitude() +" , ";
		}
		rutaStr = rutaStr.substring(0,rutaStr.length()-3)+")";
		// Lo convertimos a GIS
		ruta = Helpers.wktToGeometry(rutaStr);
		return ruta;
	}
}
