package es.udc.lbd.hermes.eventManager.strategy;

import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.ZtreamyHighHeartRate;
import es.udc.lbd.hermes.eventManager.util.Helpers;
import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.service.MeasurementService;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

@Component
public class HighHeartRateEventStrategy extends EventStrategy {
		
	@Override
	public void processEvent(Event event) {
		
		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		MeasurementService measurementService = ApplicationContextProvider.getApplicationContext().getBean("measurementService", MeasurementService.class);
		// Construir un objeto del modelo a partir del evento
		ZtreamyHighHeartRate ztreamyHighHeartRate = (ZtreamyHighHeartRate) event.getEventData();
		Measurement measurement = new Measurement();
		Geometry punto = Helpers.prepararPunto(ztreamyHighHeartRate.getLatitude(),ztreamyHighHeartRate.getLongitude());
		measurement.setPosition((Point)punto);
		measurement.setValue(ztreamyHighHeartRate.getValue());
		measurement.setTipo("High Heart Rate");
		measurement.setEventId(event.getEventId());
		// Falta decidir como se va a hacer y donde usuarioId
		// vehicleLocation.setEventId(event.getSourceId());
		measurement.setTimestamp(event.getTimestamp());
		measurementService.create(measurement);
		// Ultimo evento procesado
		eventService.create(event.getTimestamp(),event.getEventId());
	}

}