package es.enxenio.smart.citydriver.factory;

import es.enxenio.smart.citydriver.model.events.EventType;
import es.enxenio.smart.citydriver.strategy.DataSectionEventStrategy;
import es.enxenio.smart.citydriver.strategy.EventStrategy;
import es.enxenio.smart.citydriver.strategy.MeasurementEventStrategy;
import es.enxenio.smart.citydriver.strategy.VehicleLocationEventStrategy;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventFactory {

//	private Logger logger = LoggerFactory.getLogger(getClass());

	public static EventStrategy getEvent(EventType tipoEvento) {
		
		try {
			if (tipoEvento!= null) {
				switch (tipoEvento) {
				case VEHICLE_LOCATION:
					return new VehicleLocationEventStrategy();
				case HIGH_SPEED: case HIGH_ACCELERATION: case HIGH_DECELERATION: case HIGH_HEART_RATE:
					return new MeasurementEventStrategy();				
				case DATA_SECTION:
					return new DataSectionEventStrategy();		
				default:
					break;
				}
			
			} else {
				System.out.println("No está llegando ningún evento que especifique su Event-Type");
			}
		} catch (NullPointerException e) {
//			 logger.error("Excepción recuperando tipo de evento ",e);
			System.out.println("Excepción recuperando tipo de evento " + e );			
		}
		return null;
		
	}
}