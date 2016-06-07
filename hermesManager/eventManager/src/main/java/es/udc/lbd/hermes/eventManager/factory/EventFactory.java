package es.udc.lbd.hermes.eventManager.factory;

import java.util.HashMap;
import java.util.Map;

import es.udc.lbd.hermes.eventManager.strategy.ContextDataEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.DataSectionEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.DriverFeaturesEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.EventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.FullUserActivitiesEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.FullUserLocationsEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.HeartRateDataEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.HighAccelerationEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.HighDecelerationEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.HighHeartRateEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.HighSpeedEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.SleepDataEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.StepsDataEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.UserActivitiesEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.UserCaloriesExpendedEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.UserDistancesEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.UserHeartRatesEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.UserLocationsEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.UserSleepEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.UserStepsEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.VehicleLocationEventStrategy;
import es.udc.lbd.hermes.model.events.EventProcesor;

public class EventFactory {

//	private Logger logger = LoggerFactory.getLogger(getClass());
	private static Map<EventProcesor, EventStrategy> registry;
	static {
		registry = new HashMap<EventProcesor, EventStrategy>();
		registry.put(EventProcesor.VEHICLE_LOCATION, new VehicleLocationEventStrategy());
		registry.put(EventProcesor.HIGH_SPEED, new HighSpeedEventStrategy());
		registry.put(EventProcesor.HIGH_ACCELERATION, new HighAccelerationEventStrategy());
		registry.put(EventProcesor.HIGH_DECELERATION, new HighDecelerationEventStrategy());
		registry.put(EventProcesor.HIGH_HEART_RATE, new HighHeartRateEventStrategy());
		registry.put(EventProcesor.DRIVER_FEATURES, new DriverFeaturesEventStrategy());
		registry.put(EventProcesor.DATA_SECTION, new DataSectionEventStrategy());
		registry.put(EventProcesor.SLEEP_DATA, new SleepDataEventStrategy());
		registry.put(EventProcesor.STEPS_DATA, new StepsDataEventStrategy());
		registry.put(EventProcesor.CONTEXT_DATA, new ContextDataEventStrategy());
		registry.put(EventProcesor.HEART_RATE_DATA, new HeartRateDataEventStrategy());
		registry.put(EventProcesor.USER_ACTIVITIES, new UserActivitiesEventStrategy());
		registry.put(EventProcesor.USER_LOCATIONS, new UserLocationsEventStrategy());
		registry.put(EventProcesor.FULL_USER_ACTIVITIES, new FullUserActivitiesEventStrategy());
		registry.put(EventProcesor.FULL_USER_LOCATIONS, new FullUserLocationsEventStrategy());
		registry.put(EventProcesor.USER_DISTANCES, new UserDistancesEventStrategy());
		registry.put(EventProcesor.USER_STEPS, new UserStepsEventStrategy());
		registry.put(EventProcesor.USER_CALORIES_EXPENDED, new UserCaloriesExpendedEventStrategy());
		registry.put(EventProcesor.USER_HEART_RATES , new UserHeartRatesEventStrategy());
		registry.put(EventProcesor.USER_SLEEP, new UserSleepEventStrategy());
	}

	private EventFactory() {
	}
	
	public static EventStrategy getStrategy(EventProcesor tipoEvento) {
		
		EventStrategy result = null;
		if (tipoEvento!= null) {
			result = registry.get(tipoEvento);
		}
		return result;		
	}
}