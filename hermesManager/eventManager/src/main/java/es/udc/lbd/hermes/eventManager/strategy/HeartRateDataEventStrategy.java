package es.udc.lbd.hermes.eventManager.strategy;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.ZtreamyHeartRate;
import es.udc.lbd.hermes.eventManager.json.ZtreamyHeartRateData;
import es.udc.lbd.hermes.model.events.heartRateData.HeartRateData;
import es.udc.lbd.hermes.model.events.heartRateData.service.HeartRateDataService;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

@Component
public class HeartRateDataEventStrategy extends EventStrategy {
	
	@Override
	public void processEvent(Event event) {

		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		HeartRateDataService heartRateDataService = ApplicationContextProvider.getApplicationContext().getBean("heartRateDataService", HeartRateDataService.class);
		// Construir un objeto del modelo a partir del evento
		ZtreamyHeartRateData ztreamyHeartRateData = (ZtreamyHeartRateData) event.getEventData();
		Calendar dateTime = ztreamyHeartRateData.getDateTime();
		for (ZtreamyHeartRate ztreamyHeartRate : ztreamyHeartRateData.getHeartRateList()) {
			HeartRateData heartRateData = new HeartRateData();
			heartRateData.setHeartRate(ztreamyHeartRate.getHeartRate());
			ztreamyHeartRate.getTimeLog().set(dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH));
			heartRateData.setTimeLog(ztreamyHeartRate.getTimeLog());
			heartRateData.setEventId(event.getEventId());
			heartRateDataService.create(heartRateData, event.getSourceId());
		}		
		// Ultimo evento procesado
		eventService.create(event.getTimestamp(),event.getEventId());
	}
}