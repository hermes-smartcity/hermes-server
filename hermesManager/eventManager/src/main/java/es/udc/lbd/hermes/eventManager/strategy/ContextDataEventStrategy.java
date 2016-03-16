package es.udc.lbd.hermes.eventManager.strategy;

import java.util.Calendar;
import java.util.TimeZone;

import org.springframework.stereotype.Component;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.ZtreamyContextData;
import es.udc.lbd.hermes.eventManager.json.ZtreamyContextDataList;
import es.udc.lbd.hermes.model.events.contextData.ContextData;
import es.udc.lbd.hermes.model.events.contextData.service.ContextDataService;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

@Component
public class ContextDataEventStrategy extends EventStrategy {
	
	@Override
	public void processEvent(Event event) {

		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		ContextDataService contextDataService = ApplicationContextProvider.getApplicationContext().getBean("contextDataService", ContextDataService.class);
		// Construir un objeto del modelo a partir del evento
		ZtreamyContextDataList ztreamyContextData = (ZtreamyContextDataList) event.getEventData();
		Calendar dateTime = ztreamyContextData.getDateTime();
		for (ZtreamyContextData ztreamyContext : ztreamyContextData.getContextLogDetailList()) {
			ContextData contextData = new ContextData();
			contextData.setLatitude(ztreamyContext.getLatitude());
			contextData.setLongitude(ztreamyContext.getLongitude());
			contextData.setDetectedActivity(ztreamyContext.getDetectedActivity());
			contextData.setAccuracy(ztreamyContext.getAccuracy());
			
			ztreamyContext.getTimeLog().set(dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH));
			ztreamyContext.getTimeLog().setTimeZone(TimeZone.getDefault());
			contextData.setTimeLog(ztreamyContext.getTimeLog());
			contextData.setEventId(event.getEventId());
			contextDataService.create(contextData, event.getSourceId());
		}		
		// Ultimo evento procesado
		eventService.create(event.getTimestamp(),event.getEventId());
	}

}