package es.udc.lbd.hermes.eventManager.strategy;


import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.LineString;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.ZtreamyDataSection;
import es.udc.lbd.hermes.eventManager.json.ZtreamyDriverFeatures;
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
		// Construir un objeto del modelo a partir del evento
		ZtreamyDataSection ztreamyDataSection = (ZtreamyDataSection) event.getEventData();
		DataSection dataSection = new DataSection();
		dataSection.setMinSpeed(ztreamyDataSection.getMedianSpeed());
		dataSection.setMaxSpeed(ztreamyDataSection.getMaxSpeed());
		dataSection.setMedianSpeed(ztreamyDataSection.getMedianSpeed());
		dataSection.setAverageSpeed(ztreamyDataSection.getAverageSpeed());
		dataSection.setAverageRR(ztreamyDataSection.getAverageRR());
		dataSection.setAverageHeartRate(ztreamyDataSection.getAverageHeartRate());
		dataSection.setStandardDeviationSpeed(ztreamyDataSection.getStandardDeviationSpeed());
		dataSection.setStandardDeviationRR(ztreamyDataSection.getStandardDeviationRR());
		dataSection.setStandardDeviationHeartRate(ztreamyDataSection.getStandardDeviationHeartRate());
		dataSection.setPke(ztreamyDataSection.getPke());
		// TODO Falta decidir como se va a hacer
		dataSection.setRoadSection((LineString) Helpers.prepararRuta(ztreamyDataSection.getRoadSection()));
		dataSection.setEventId(event.getEventId());
		// Falta decidir como se va a hacer y donde usuarioId
		// vehicleLocation.setEventId(event.getSourceId());
		dataSection.setTimestamp(event.getTimestamp());
		dataSectionService.create(dataSection, event.getSourceId());
		eventService.create(event.getTimestamp(),event.getEventId());		
	}
}