package es.udc.lbd.hermes.eventManager.strategy;

import org.springframework.stereotype.Component;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.ZtreamyUserSleep;
import es.udc.lbd.hermes.eventManager.json.ZtreamyUserSleepList;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.events.usersleep.UserSleep;
import es.udc.lbd.hermes.model.events.usersleep.service.UserSleepService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

@Component
public class UserSleepEventStrategy extends EventStrategy{

	@Override
	public void processEvent(Event event) {

		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		UserSleepService userSleepService = ApplicationContextProvider.getApplicationContext().getBean("userSleepService", UserSleepService.class);
		
		ZtreamyUserSleepList ztreamyUserSleepList = (ZtreamyUserSleepList) event.getEventData();
		for (ZtreamyUserSleep ztreamyUserSleep : ztreamyUserSleepList.getUserSleepList()) {
			UserSleep userSleep = new UserSleep();			
			userSleep.setName(ztreamyUserSleep.getName());			
			userSleep.setStartTime(ztreamyUserSleep.getStartTime());
			userSleep.setEndTime(ztreamyUserSleep.getEndTime());
			userSleep.setEventId(event.getEventId());
			userSleepService.create(userSleep, event.getSourceId());
		}
		
		// Ultimo evento procesado
		eventService.create(event.getTimestamp(),event.getEventId());
	}
}
