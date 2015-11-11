package es.udc.lbd.hermes.eventManager.strategy;
import org.springframework.stereotype.Component;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.ZtreamyDriverFeatures;
import es.udc.lbd.hermes.eventManager.util.Helpers;
import es.udc.lbd.hermes.model.events.driverFeatures.DriverFeatures;
import es.udc.lbd.hermes.model.events.driverFeatures.service.DriverFeaturesService;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;



@Component
public class DriverFeaturesEventStrategy extends EventStrategy {

	@Override
	public void processEvent(Event event) {
		
		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		DriverFeaturesService driverFeaturesService = ApplicationContextProvider.getApplicationContext().getBean("driverFeaturesService", DriverFeaturesService.class);

		DriverFeatures driverFeatures = Helpers.procesaEvento((ZtreamyDriverFeatures) event.getEventData());

		driverFeaturesService.create(driverFeatures);
		eventService.create(event.getTimestamp(),event.getEventId());
		
	}

}