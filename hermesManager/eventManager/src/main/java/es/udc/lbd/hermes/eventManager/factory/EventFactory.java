package es.udc.lbd.hermes.eventManager.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.strategy.ContextDataEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.DataSectionEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.DriverFeaturesEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.EventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.FullUserActivitiesEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.FullUserCaloriesExpendedEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.FullUserDistancesEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.FullUserHeartRatesEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.FullUserLocationsEventStrategy;
import es.udc.lbd.hermes.eventManager.strategy.FullUserStepsEventStrategy;
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
import es.udc.lbd.hermes.model.events.EventProcesor;

public class EventFactory {

	private static Logger logger = Logger.getLogger(EventFactory.class);
	private static Map<EventProcesor, Class<? extends EventStrategy>> registry;
	static {
		registry = new HashMap<EventProcesor, Class<? extends EventStrategy>>();
		registry.put(EventProcesor.HIGH_SPEED, HighSpeedEventStrategy.class);
		registry.put(EventProcesor.HIGH_ACCELERATION, HighAccelerationEventStrategy.class);
		registry.put(EventProcesor.HIGH_DECELERATION, HighDecelerationEventStrategy.class);
		registry.put(EventProcesor.HIGH_HEART_RATE, HighHeartRateEventStrategy.class);
		registry.put(EventProcesor.DRIVER_FEATURES, DriverFeaturesEventStrategy.class);
		registry.put(EventProcesor.DATA_SECTION, DataSectionEventStrategy.class);
		registry.put(EventProcesor.SLEEP_DATA, SleepDataEventStrategy.class);
		registry.put(EventProcesor.STEPS_DATA, StepsDataEventStrategy.class);
		registry.put(EventProcesor.CONTEXT_DATA, ContextDataEventStrategy.class);
		registry.put(EventProcesor.HEART_RATE_DATA, HeartRateDataEventStrategy.class);
		registry.put(EventProcesor.USER_ACTIVITIES, UserActivitiesEventStrategy.class);
		registry.put(EventProcesor.USER_LOCATIONS, UserLocationsEventStrategy.class);
		registry.put(EventProcesor.FULL_USER_ACTIVITIES, FullUserActivitiesEventStrategy.class);
		registry.put(EventProcesor.FULL_USER_LOCATIONS, FullUserLocationsEventStrategy.class);
		registry.put(EventProcesor.USER_DISTANCES, UserDistancesEventStrategy.class);
		registry.put(EventProcesor.USER_STEPS, UserStepsEventStrategy.class);
		registry.put(EventProcesor.USER_CALORIES_EXPENDED, UserCaloriesExpendedEventStrategy.class);
		registry.put(EventProcesor.USER_HEART_RATES, UserHeartRatesEventStrategy.class);
		registry.put(EventProcesor.USER_SLEEP, UserSleepEventStrategy.class);
		registry.put(EventProcesor.FULL_USER_DISTANCES, FullUserDistancesEventStrategy.class);
		registry.put(EventProcesor.FULL_USER_STEPS, FullUserStepsEventStrategy.class);
		registry.put(EventProcesor.FULL_USER_CALORIES_EXPENDED, FullUserCaloriesExpendedEventStrategy.class);
		registry.put(EventProcesor.FULL_USER_HEART_RATES, FullUserHeartRatesEventStrategy.class);		
	}

	private EventFactory() {
	}
	
	public static EventStrategy getStrategy(EventProcesor tipoEvento, Event event) {
		
		EventStrategy result = null;
		if (tipoEvento!= null) {
			Class<? extends EventStrategy> resultClass = registry.get(tipoEvento);
			if (resultClass!=null) {
				try {
					result = resultClass.newInstance();
					Method method;
					method = result.getClass().getMethod("setEvent", Event.class);
					method.invoke(result, event);
				} catch (InstantiationException e) {
					logger.error(e);				
				} catch (IllegalAccessException e) {
					logger.error(e);				
				} catch (SecurityException e) {
					logger.error(e);				
				} catch (NoSuchMethodException e) {
					logger.error(e);				
				} catch (IllegalArgumentException e) {
					logger.error(e);
				} catch (InvocationTargetException e) {
					logger.error(e);
				}
			}
		}
		return result;
	}
}