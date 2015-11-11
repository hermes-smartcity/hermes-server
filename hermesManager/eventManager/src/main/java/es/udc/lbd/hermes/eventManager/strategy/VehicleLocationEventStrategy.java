package es.udc.lbd.hermes.eventManager.strategy;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.ZtreamyDataSection;
import es.udc.lbd.hermes.eventManager.json.ZtreamyVehicleLocation;
import es.udc.lbd.hermes.eventManager.util.Helpers;
import es.udc.lbd.hermes.model.events.EventType;
import es.udc.lbd.hermes.model.events.eventoProcesado.EventoProcesado;
import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
import es.udc.lbd.hermes.model.events.vehicleLocation.service.VehicleLocationService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

@Component
public class VehicleLocationEventStrategy extends EventStrategy {

	@Override
	public void processEvent(Event event) {

		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService",
				EventService.class);
		VehicleLocationService vehicleLocationService = ApplicationContextProvider.getApplicationContext()
				.getBean("vehicleLocationService", VehicleLocationService.class);

		VehicleLocation vehicleLocation = Helpers.procesaEvento((ZtreamyVehicleLocation) event.getEventData());
		vehicleLocation.setEventId(event.getEventId());
		// Falta decidir como se va a hacer y donde usuarioId
		// vehicleLocation.setEventId(event.getSourceId());
		vehicleLocation.setTimestamp(event.getTimestamp());
		vehicleLocationService.create(vehicleLocation);
		// Ultimo evento procesado
		eventService.create(event.getTimestamp(),event.getEventId());
	}

}