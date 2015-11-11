package es.udc.lbd.hermes.eventManager.strategy;


import org.springframework.stereotype.Component;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.ZtreamyDataSection;
import es.udc.lbd.hermes.eventManager.util.Helpers;
import es.udc.lbd.hermes.model.events.dataSection.DataSection;
import es.udc.lbd.hermes.model.events.dataSection.service.DataSectionService;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

@Component
public class DataSectionEventStrategy extends EventStrategy {

	@Override
	public void processEvent(Event event) {
		
		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		DataSectionService dataSectionService = ApplicationContextProvider.getApplicationContext().getBean("dataSectionService", DataSectionService.class);

		DataSection dataSection = Helpers.procesaEvento((ZtreamyDataSection) event.getEventData());
		dataSection.setEventId(event.getEventId());
		// Falta decidir como se va a hacer y donde usuarioId
		// vehicleLocation.setEventId(event.getSourceId());
		dataSection.setTimestamp(event.getTimestamp());
		dataSectionService.create(dataSection);
		eventService.create(event.getTimestamp(),event.getEventId());
		
	}

}