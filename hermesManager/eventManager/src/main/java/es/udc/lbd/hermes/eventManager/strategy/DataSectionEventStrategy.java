package es.udc.lbd.hermes.eventManager.strategy;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.LineString;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.EventParser;
import es.udc.lbd.hermes.eventManager.json.ZtreamyDataSection;
import es.udc.lbd.hermes.eventManager.util.Helpers;
import es.udc.lbd.hermes.model.events.dataSection.DataSection;
import es.udc.lbd.hermes.model.events.dataSection.service.DataSectionService;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

@Component
public class DataSectionEventStrategy extends EventStrategy {

	private static Logger logger = Logger.getLogger(DataSectionEventStrategy.class);
	
	@Override
	public void processEvent(Event event) {
		
		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		DataSectionService dataSectionService = ApplicationContextProvider.getApplicationContext().getBean("dataSectionService", DataSectionService.class);		
		// Construir un objeto del modelo a partir del evento
		ZtreamyDataSection ztreamyDataSection = (ZtreamyDataSection) event.getEventData();
		if (ztreamyDataSection.getRoadSection().size() >= 2) {
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
			dataSection.setNumHighDecelerations(ztreamyDataSection.getNumHighDecelerations());
			dataSection.setNumHighAccelerations(ztreamyDataSection.getNumHighAccelerations());
			dataSection.setAverageAcceleration(ztreamyDataSection.getAverageAcceleration());
			dataSection.setAverageDeceleration(ztreamyDataSection.getAverageDeceleration());
			dataSection.setRrSection(ztreamyDataSection.getRrSection());
			dataSection.setPke(ztreamyDataSection.getPke());
			// TODO Falta decidir como se va a hacer
			dataSection.setRoadSection((LineString) Helpers.prepararRuta(ztreamyDataSection.getRoadSection()));
			dataSection.setAccuracy(Helpers.prepararPrecision(ztreamyDataSection.getRoadSection()));
			dataSection.setSpeed(Helpers.prepararVelocidad(ztreamyDataSection.getRoadSection()));
			dataSection.setEventId(event.getEventId());
	
			dataSection.setTimestamp(event.getTimestamp());
			dataSectionService.create(dataSection, event.getSourceId());
		} else {
			EventParser parser = new EventParser();
			String eventAsString = parser.prettyPrint(event);
			logger.warn("Evento descartado. DataSection con un único punto." + eventAsString);
		}
		eventService.create(event.getTimestamp(),event.getEventId());		
	}
}