package es.udc.lbd.hermes.model.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import es.udc.lbd.hermes.model.events.EventType;
import es.udc.lbd.hermes.model.events.dataSection.DataSection;
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

	public static DataSection prepararDataSection(EventHelper eventHelper) {
		DataSection dataSection = new DataSection();
		dataSection.setUsuario(eventHelper.getUsuario());
		dataSection.setTimestamp(eventHelper.getTimestamp());
		dataSection.setEventId(eventHelper.getEventId());		
		dataSection.setMinHeartRate(eventHelper.getMinHeartRate());
		dataSection.setMaxBeatBeat(eventHelper.getMaxBeatBeat());		
		dataSection.setStandardDeviationSpeed(eventHelper.getStandardDeviationSpeed());
		dataSection.setMinBeatBeat(eventHelper.getMinBeatBeat());
		dataSection.setMinSpeed(eventHelper.getMinSpeed());				
		dataSection.setAverageSpeed(eventHelper.getAverageSpeed());				
		dataSection.setStandardDeviationBeatBeat(eventHelper.getStandardDeviationBeatBeat());				
		dataSection.setHeartRate(eventHelper.getHeartRate()); 					
		dataSection.setMedianSpeed(eventHelper.getMedianSpeed());				
		dataSection.setStandardDeviationHeartRate(eventHelper.getStandardDeviationHeartRate());				
		dataSection.setMaxSpeed(eventHelper.getMaxSpeed());				
		dataSection.setPke(eventHelper.getPke());
		dataSection.setRoadSection(eventHelper.getRoadSection());
		dataSection.setMedianHeartRate(eventHelper.getMedianHeartRate());
		dataSection.setMeanBeatBeat(eventHelper.getMeanBeatBeat());
		dataSection.setMedianBeatBeat(eventHelper.getMedianBeatBeat());
		return dataSection;
	}
		
	public static VehicleLocation prepararVehicleLocation(EventHelper eventHelper) {
		VehicleLocation vehicleLocation = new VehicleLocation();	
		vehicleLocation.setUsuario(eventHelper.getUsuario());
		vehicleLocation.setTimestamp(eventHelper.getTimestamp());
		vehicleLocation.setEventId(eventHelper.getEventId());	
		vehicleLocation.setPosition(eventHelper.getPosition());
		return vehicleLocation;
	}
	
	public static Measurement prepararMeasurement(EventHelper eventHelper){
		Measurement measurement = new Measurement();
		measurement.setUsuario(eventHelper.getUsuario());
		measurement.setPosition(eventHelper.getPosition());
		measurement.setTimestamp(eventHelper.getTimestamp());
		measurement.setEventId(eventHelper.getEventId());
		measurement.setTipo("High Speed");
		return measurement;
	}

	public static EventoProcesado prepararEventoProcesado(EventHelper eventHelper){
		EventoProcesado eventoProcesado = new EventoProcesado();
		eventoProcesado.setTimestamp(eventHelper.getTimestamp());
		eventoProcesado.setEventId(eventHelper.getEventId());
		
		return eventoProcesado;
	}
	
	public static String prepararParametros(EventType tipoEvento){
		String param="";

		switch (tipoEvento) {		
		case HIGH_SPEED:
			param="High Speed";
			break;
		case HIGH_ACCELERATION:
			param="High Acceleration";
			break;
		case HIGH_DECELERATION:
			param="High Deceleration";
			break;
		case HIGH_HEART_RATE:		
			param="High Heart Rate";
			break;		
		default:
			break;
		}
		return param;	
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
}
