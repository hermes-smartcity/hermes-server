package es.udc.lbd.hermes.eventManager.factory;

import java.util.HashMap;
import java.util.Map;

import es.udc.lbd.hermes.eventManager.strategy.ContextDataEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.DataSectionEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.DriverFeaturesEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.EventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.HeartRateDataEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.HighAccelerationEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.HighDecelerationEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.HighHeartRateEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.HighSpeedEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.SleepDataEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.StepsDataEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.UserActivitiesEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.UserLocationsEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.VehicleLocationEventStrategy;
import es.udc.lbd.hermes.model.events.EventType;

public class EventFactory {

//	private Logger logger = LoggerFactory.getLogger(getClass());
	private static Map<EventType, EventStrategy> registry;
	static {
		registry = new HashMap<EventType, EventStrategy>();
		registry.put(EventType.VEHICLE_LOCATION, new VehicleLocationEventStrategy());
		registry.put(EventType.HIGH_SPEED, new HighSpeedEventStrategy());
		registry.put(EventType.HIGH_ACCELERATION, new HighAccelerationEventStrategy());
		registry.put(EventType.HIGH_DECELERATION, new HighDecelerationEventStrategy());
		registry.put(EventType.HIGH_HEART_RATE, new HighHeartRateEventStrategy());
		registry.put(EventType.DRIVER_FEATURES, new DriverFeaturesEventStrategy());
		registry.put(EventType.DATA_SECTION, new DataSectionEventStrategy());
		registry.put(EventType.SLEEP_DATA, new SleepDataEventStrategy());
		registry.put(EventType.STEPS_DATA, new StepsDataEventStrategy());
		registry.put(EventType.CONTEXT_DATA, new ContextDataEventStrategy());
		registry.put(EventType.HEART_RATE_DATA, new HeartRateDataEventStrategy());
		registry.put(EventType.USER_ACTIVITIES, new UserActivitiesEventStrategy());
		registry.put(EventType.USER_LOCATIONS, new UserLocationsEventStrategy());
	}

	private EventFactory() {
	}
	
	public static EventStrategy getStrategy(EventType tipoEvento) {
		
		EventStrategy result = null;
		if (tipoEvento!= null) {
			result = registry.get(tipoEvento);
		}
		return result;		
	}
}