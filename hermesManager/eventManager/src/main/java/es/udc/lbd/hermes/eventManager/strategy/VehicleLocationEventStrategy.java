package es.udc.lbd.hermes.eventManager.strategy;

import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.ZtreamyVehicleLocation;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
import es.udc.lbd.hermes.model.events.vehicleLocation.service.VehicleLocationService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;
import es.udc.lbd.hermes.model.util.HelpersModel;

@Component
public class VehicleLocationEventStrategy extends EventStrategy {

	@Override
	public void processEvent(Event event) {

		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		VehicleLocationService vehicleLocationService = ApplicationContextProvider.getApplicationContext().getBean("vehicleLocationService", VehicleLocationService.class);
		// Construir un objeto del modelo a partir del evento
		ZtreamyVehicleLocation ztreamyVehicleLocation = (ZtreamyVehicleLocation) event.getEventData();
		VehicleLocation vehicleLocation = new VehicleLocation();	
		Geometry punto = HelpersModel.prepararPunto(ztreamyVehicleLocation.getLatitude(), ztreamyVehicleLocation.getLongitude());
		vehicleLocation.setPosition((Point)punto);
		vehicleLocation.setEventId(event.getEventId());
		
		vehicleLocation.setTimestamp(ztreamyVehicleLocation.getTimeStamp());
		vehicleLocationService.create(vehicleLocation, event.getSourceId());
		
		// Ultimo evento procesado
		eventService.create(event.getTimestamp(),event.getEventId());
	}

}