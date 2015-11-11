package es.udc.lbd.hermes.eventManager.factory;

import java.util.HashMap;
import java.util.Map;

import es.udc.lbd.hermes.eventManager.strategy.DataSectionEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.DriverFeaturesEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.EventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.HighAccelerationEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.HighDecelerationEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.HighHeartRateEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.HighSpeedEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.VehicleLocationEventStrategy;
import es.udc.lbd.hermes.model.events.EventType;

public class EventFactory {

//	private Logger logger = LoggerFactory.getLogger(getClass());
	private static Map<EventType, EventStrategy> registry = new HashMap<EventType, EventStrategy>();

	private EventFactory() {
		registry.put(EventType.VEHICLE_LOCATION, new VehicleLocationEventStrategy());
		registry.put(EventType.HIGH_SPEED, new HighSpeedEventStrategy());
		registry.put(EventType.HIGH_ACCELERATION, new HighAccelerationEventStrategy());
		registry.put(EventType.HIGH_DECELERATION, new HighDecelerationEventStrategy());
		registry.put(EventType.HIGH_HEART_RATE, new HighHeartRateEventStrategy());
		registry.put(EventType.DRIVER_FEATURES, new DriverFeaturesEventStrategy());
		registry.put(EventType.DATA_SECTION, new DataSectionEventStrategy());
	}
	
	public static EventStrategy getStrategy(EventType tipoEvento) {
		
		EventStrategy result = null;
		try {
			if (tipoEvento!= null) {
				result = registry.get(tipoEvento);
			} else {
				System.out.println("No está llegando ningún evento que especifique su Event-Type");
			}
		} catch (NullPointerException e) {
//			 logger.error("Excepción recuperando tipo de evento ",e);	
		}
		return null;		
	}
}