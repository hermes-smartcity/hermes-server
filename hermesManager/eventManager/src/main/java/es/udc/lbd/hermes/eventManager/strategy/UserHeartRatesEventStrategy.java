package es.udc.lbd.hermes.eventManager.strategy;

import org.springframework.stereotype.Component;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.ZtreamyUserHeartRates;
import es.udc.lbd.hermes.eventManager.json.ZtreamyUserHeartRatesList;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.events.userheartrates.UserHeartRates;
import es.udc.lbd.hermes.model.events.userheartrates.service.UserHeartRatesService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

@Component
public class UserHeartRatesEventStrategy extends EventStrategy{

	@Override
	public void processEvent(Event event) {

		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		UserHeartRatesService userHeartRatesService = ApplicationContextProvider.getApplicationContext().getBean("userHeartRatesService", UserHeartRatesService.class);
		
		ZtreamyUserHeartRatesList ztreamyUserHeartRatesList = (ZtreamyUserHeartRatesList) event.getEventData();
		for (ZtreamyUserHeartRates ztreamyUserHeartRates : ztreamyUserHeartRatesList.getUserHeartRatesList()) {
			UserHeartRates userHeartRates = new UserHeartRates();			
			userHeartRates.setBpm(ztreamyUserHeartRates.getBpm());			
			userHeartRates.setStartTime(ztreamyUserHeartRates.getStartTime());
			userHeartRates.setEndTime(ztreamyUserHeartRates.getEndTime());
			userHeartRates.setEventId(event.getEventId());
			userHeartRatesService.create(userHeartRates, event.getSourceId());
		}
		
		// Ultimo evento procesado
		eventService.create(event.getTimestamp(),event.getEventId());
	}
}
