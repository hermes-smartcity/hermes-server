package es.udc.lbd.hermes.eventManager.strategy;

import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.EventData;
import es.udc.lbd.hermes.eventManager.json.EventDataArray;
import es.udc.lbd.hermes.eventManager.json.ZtreamyUserLocation;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.events.userlocations.UserLocations;
import es.udc.lbd.hermes.model.events.userlocations.service.UserLocationsService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;
import es.udc.lbd.hermes.model.util.HelpersModel;

@Component
public class UserLocationsEventStrategy extends EventStrategy{

	@Override
	public void processEvent(Event event) {

		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		UserLocationsService userLocationsService = ApplicationContextProvider.getApplicationContext().getBean("userLocationsService", UserLocationsService.class);
		
		EventDataArray eventDataArray = (EventDataArray)event.getEventData();
		for (EventData eventData : eventDataArray.getEvents()) {
			UserLocations userLocations = new UserLocations();
			
			userLocations.setAccuracy(((ZtreamyUserLocation)eventData).getAccuracy());
			
			Geometry punto = HelpersModel.prepararPunto(((ZtreamyUserLocation)eventData).getLatitude(), ((ZtreamyUserLocation)eventData).getLongitude());
			userLocations.setPosition((Point)punto);
			
			userLocations.setStartTime(((ZtreamyUserLocation)eventData).getStartTime());
			userLocations.setEndTime(((ZtreamyUserLocation)eventData).getEndTime());
			userLocations.setEventId(event.getEventId());
			userLocationsService.create(userLocations, event.getSourceId());
		}
		
		// Ultimo evento procesado
		eventService.create(event.getTimestamp(),event.getEventId());
	}
}
