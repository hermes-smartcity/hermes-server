package es.udc.lbd.hermes.eventManager.strategy;

import org.springframework.stereotype.Component;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.ZtreamyUserCaloriesExpended;
import es.udc.lbd.hermes.eventManager.json.ZtreamyUserCaloriesExpendedList;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.events.usercaloriesexpended.UserCaloriesExpended;
import es.udc.lbd.hermes.model.events.usercaloriesexpended.service.UserCaloriesExpendedService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

@Component
public class UserCaloriesExpendedEventStrategy extends EventStrategy{

	@Override
	public void processEvent(Event event) {

		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		UserCaloriesExpendedService userCaloriesExpendedService = ApplicationContextProvider.getApplicationContext().getBean("userCaloriesExpendedService", UserCaloriesExpendedService.class);
		
		ZtreamyUserCaloriesExpendedList ztreamyUserCaloriesExpendedList = (ZtreamyUserCaloriesExpendedList) event.getEventData();
		for (ZtreamyUserCaloriesExpended ztreamyUserCaloriesExpended : ztreamyUserCaloriesExpendedList.getUserCaloriesExpendedList()) {
			UserCaloriesExpended userCaloriesExpended = new UserCaloriesExpended();			
			userCaloriesExpended.setCalories(ztreamyUserCaloriesExpended.getCalories());			
			userCaloriesExpended.setStartTime(ztreamyUserCaloriesExpended.getStartTime());
			userCaloriesExpended.setEndTime(ztreamyUserCaloriesExpended.getEndTime());
			userCaloriesExpended.setEventId(event.getEventId());
			userCaloriesExpendedService.create(userCaloriesExpended, event.getSourceId());
		}
		
		// Ultimo evento procesado
		eventService.create(event.getTimestamp(),event.getEventId());
	}
}
