package es.udc.lbd.hermes.eventManager.strategy;

import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.ZtreamyHighAcceleration;
import es.udc.lbd.hermes.eventManager.util.Helpers;
import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.service.MeasurementService;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

@Component
public class HighAccelerationEventStrategy extends EventStrategy {
		
	@Override
	public void processEvent(Event event) {
		
		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		MeasurementService measurementService = ApplicationContextProvider.getApplicationContext().getBean("measurementService", MeasurementService.class);
		// Construir un objeto del modelo a partir del evento
		ZtreamyHighAcceleration ztreamyHighAcceleration = (ZtreamyHighAcceleration) event.getEventData();
		Measurement measurement = new Measurement();
		Geometry punto = Helpers.prepararPunto(ztreamyHighAcceleration.getLatitude(),ztreamyHighAcceleration.getLongitude());
		measurement.setPosition((Point)punto);
		measurement.setValue(ztreamyHighAcceleration.getValue());
		measurement.setTipo("High Acceleration");
		measurement.setEventId(event.getEventId());

		measurement.setTimestamp(event.getTimestamp());
		measurementService.create(measurement, event.getSourceId());
		// Ultimo evento procesado
		eventService.create(event.getTimestamp(),event.getEventId());
	}

}