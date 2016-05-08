package es.udc.lbd.hermes.eventManager.strategy;

import org.springframework.stereotype.Component;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.ZtreamyUserActivity;
import es.udc.lbd.hermes.eventManager.json.ZtreamyUserActivityList;
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
		
		ZtreamyUserActivityList ztreamyUserActivityList = (ZtreamyUserActivityList) event.getEventData();
		for (ZtreamyUserActivity ztreamyUserActivity : ztreamyUserActivityList.getUserActivitiesList()) {
			UserActivities userActivities = new UserActivities();			
			userActivities.setName(ztreamyUserActivity.getName());			
			userActivities.setStartTime(ztreamyUserActivity.getStartTime());
			userActivities.setEndTime(ztreamyUserActivity.getEndTime());
			userActivities.setEventId(event.getEventId());
			userActivitiesService.create(userActivities, event.getSourceId());
		}
		
		// Ultimo evento procesado
		eventService.create(event.getTimestamp(),event.getEventId());
	}
}
