package es.udc.lbd.hermes.eventManager.strategy;

import java.util.Calendar;
import java.util.TimeZone;

import org.springframework.stereotype.Component;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.ZtreamyContextData;
import es.udc.lbd.hermes.eventManager.json.ZtreamyContextDataList;
import es.udc.lbd.hermes.eventManager.json.ZtreamySteps;
import es.udc.lbd.hermes.eventManager.json.ZtreamyStepsData;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.events.stepsData.StepsData;
import es.udc.lbd.hermes.model.events.stepsData.service.StepsDataService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

@Component
public class ContextDataEventStrategy extends EventStrategy {
	
	@Override
	public void processEvent(Event event) {

		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		//StepsDataService stepsDataService = ApplicationContextProvider.getApplicationContext().getBean("stepsDataService", StepsDataService.class);
		// Construir un objeto del modelo a partir del evento
		ZtreamyContextDataList ztreamyContextDataList = (ZtreamyContextDataList) event.getEventData();
		Calendar dateTime = ztreamyContextDataList.getDateTime();
		for (ZtreamyContextData ztreamyContext : ztreamyContextDataList.getContextLogDetailList()) {
			//StepsData stepsData = new StepsData();
			//stepsData.setSteps(ztreamySteps.getSteps());
			//ztreamySteps.getTimeLog().set(dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH));
			//ztreamySteps.getTimeLog().setTimeZone(TimeZone.getDefault());
			//stepsData.setTimeLog(ztreamySteps.getTimeLog());
			//stepsData.setEventId(event.getEventId());
			//stepsDataService.create(stepsData, event.getSourceId());
		}		
		// Ultimo evento procesado
		//eventService.create(event.getTimestamp(),event.getEventId());
	}

}