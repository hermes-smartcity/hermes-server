package es.udc.lbd.hermes.eventManager.factory;

import es.udc.lbd.hermes.eventManager.strategy.DataSectionEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.EventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.MeasurementEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.VehicleLocationEventStrategy;
import es.udc.lbd.hermes.model.events.EventType;

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
		}
		return null;
		
	}
}