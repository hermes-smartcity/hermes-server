package es.udc.lbd.hermes.eventManager.strategy;

import java.util.Calendar;
import java.util.TimeZone;

import org.springframework.stereotype.Component;

import es.udc.lbd.hermes.eventManager.json.ZtreamySleepData;
import es.udc.lbd.hermes.model.events.sleepData.SleepData;
import es.udc.lbd.hermes.model.events.sleepData.service.SleepDataService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

@Component
public class SleepDataEventStrategy extends EventStrategy {
	
	@Override
	public void run() {

		start();
		SleepDataService sleepDataService = ApplicationContextProvider.getApplicationContext().getBean("sleepDataService", SleepDataService.class);
		// Construir un objeto del modelo a partir del evento
		ZtreamySleepData ztreamysleepData = (ZtreamySleepData) event.getEventData();
		SleepData sleepData = new SleepData();
		sleepData.setAwakenings(ztreamysleepData.getAwakenings());
		sleepData.setMinutesAsleep(ztreamysleepData.getMinutesAsleep());
		sleepData.setMinutesInBed(ztreamysleepData.getMinutesInBed());
		ztreamysleepData.getStartTime().set(ztreamysleepData.getDateTime().get(Calendar.YEAR), ztreamysleepData.getDateTime().get(Calendar.MONTH), ztreamysleepData.getDateTime().get(Calendar.DAY_OF_MONTH));
		ztreamysleepData.getStartTime().setTimeZone(TimeZone.getDefault());
		sleepData.setStartTime(ztreamysleepData.getStartTime());		
		ztreamysleepData.getEndTime().set(ztreamysleepData.getDateTime().get(Calendar.YEAR), ztreamysleepData.getDateTime().get(Calendar.MONTH), ztreamysleepData.getDateTime().get(Calendar.DAY_OF_MONTH));
		ztreamysleepData.getEndTime().setTimeZone(TimeZone.getDefault());
		sleepData.setEndTime(ztreamysleepData.getEndTime());
		sleepData.setEventId(event.getEventId());
		sleepDataService.create(sleepData, event.getSourceId());
		end();
	}

}