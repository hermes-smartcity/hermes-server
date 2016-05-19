package es.udc.lbd.hermes.eventManager.strategy;

import java.util.Calendar;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.model.efficiencytest.service.EfficiencyTestService;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

public abstract class EventStrategy implements Runnable{
	protected Event event;
	private long startTime;
	private long eventTime;		
	
	public void setEvent(Event event) {
		this.event = event;
	}
	
	public void start() {
		startTime = System.nanoTime();
		eventTime = event.getTimestamp().getTimeInMillis();		
	}

	public void end() {
		long totalTime = System.nanoTime() - startTime;
		long delay = eventTime != -1 ? System.currentTimeMillis() - eventTime : 0;
		EfficiencyTestService efficiencyTestService = ApplicationContextProvider.getApplicationContext().getBean("efficiencyTestService", EfficiencyTestService.class);
		efficiencyTestService.create(event.getEventType(), 0l, Calendar.getInstance(), delay, -1l, totalTime, true);
		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		// Ultimo evento procesado
		eventService.create(event.getTimestamp(),event.getEventId());		
	}
}
