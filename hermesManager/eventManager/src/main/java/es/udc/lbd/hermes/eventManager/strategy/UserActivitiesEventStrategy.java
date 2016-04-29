package es.udc.lbd.hermes.eventManager.strategy;

import org.springframework.stereotype.Component;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.EventData;
import es.udc.lbd.hermes.eventManager.json.EventDataArray;
import es.udc.lbd.hermes.eventManager.json.ZtreamyUserActivity;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.events.useractivities.UserActivities;
import es.udc.lbd.hermes.model.events.useractivities.service.UserActivitiesService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

@Component
public class UserActivitiesEventStrategy extends EventStrategy{

	@Override
	public void processEvent(Event event) {

		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		UserActivitiesService userActivitiesService = ApplicationContextProvider.getApplicationContext().getBean("userActivitiesService", UserActivitiesService.class);
		
		EventDataArray eventDataArray = (EventDataArray)event.getEventData();
		for (EventData eventData : eventDataArray.getEvents()) {
			UserActivities userActivities = new UserActivities();
			
			userActivities.setName(((ZtreamyUserActivity)eventData).getName());
			
			userActivities.setStartTime(((ZtreamyUserActivity)eventData).getStartTime());
			userActivities.setEndTime(((ZtreamyUserActivity)eventData).getEndTime());
			userActivities.setEventId(event.getEventId());
			userActivitiesService.create(userActivities, event.getSourceId());
		}
		
		
		// Ultimo evento procesado
		eventService.create(event.getTimestamp(),event.getEventId());
	}
}
