package es.udc.lbd.hermes.eventManager.strategy;

import org.springframework.stereotype.Component;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.ZtreamyDataSection;
import es.udc.lbd.hermes.eventManager.json.ZtreamyHighAcceleration;
import es.udc.lbd.hermes.eventManager.json.ZtreamyHighDeceleration;
import es.udc.lbd.hermes.eventManager.json.ZtreamyHighHeartRate;
import es.udc.lbd.hermes.eventManager.json.ZtreamyHighSpeed;
import es.udc.lbd.hermes.eventManager.json.ZtreamyVehicleLocation;
import es.udc.lbd.hermes.eventManager.util.Helpers;
import es.udc.lbd.hermes.model.events.EventType;
import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.service.MeasurementService;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

@Component
public class MeasurementEventStrategy extends EventStrategy {
		
	@Override
	public void processEvent(Event event) {
		
		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		MeasurementService measurementService = ApplicationContextProvider.getApplicationContext().getBean("measurementService", MeasurementService.class);
		Measurement measurement = new Measurement();
		
		switch (EventType.getTipo(event.getEventType())) {
		case HIGH_ACCELERATION:
			measurement = Helpers.procesaEvento((ZtreamyHighAcceleration) event.getEventData());
			return;
		case HIGH_DECELERATION: 
			measurement = Helpers.procesaEvento((ZtreamyHighDeceleration) event.getEventData());
			return;
		case HIGH_SPEED:
			measurement = Helpers.procesaEvento((ZtreamyHighSpeed) event.getEventData());
			return;
		case HIGH_HEART_RATE:
			measurement = Helpers.procesaEvento((ZtreamyHighHeartRate) event.getEventData());
			return;
		default:
			break;
		}		
		
		measurement.setEventId(event.getEventId());
		// Falta decidir como se va a hacer y donde usuarioId
		// vehicleLocation.setEventId(event.getSourceId());
		measurement.setTimestamp(event.getTimestamp());
		measurementService.create(measurement);
		// Ultimo evento procesado
		eventService.create(event.getTimestamp(),event.getEventId());
	}

}