package es.enxenio.smart.eventManager.strategy;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import es.enxenio.smart.model.util.ApplicationContextProvider;
import es.enxenio.smart.model.events.EventType;
import es.enxenio.smart.model.events.eventoProcesado.EventoProcesado;
import es.enxenio.smart.model.events.service.EventService;
import es.enxenio.smart.model.events.vehicleLocation.VehicleLocation;
import es.enxenio.smart.model.events.vehicleLocation.service.VehicleLocationService;
import es.enxenio.smart.model.util.EventHelper;
import es.enxenio.smart.model.util.Helpers;


@Component
public class VehicleLocationEventStrategy extends EventStrategy {
	
	@Override
	public void processEvent(JSONObject evento) {
		
		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		VehicleLocationService vehicleLocationService = ApplicationContextProvider.getApplicationContext().getBean("vehicleLocationService", VehicleLocationService.class);			

		JSONObject datosBodyJSON = (JSONObject) evento.get("Body");
		
		JSONObject datosLocationJSON = (JSONObject) datosBodyJSON.get("Location");
		EventHelper eventHelper = new EventHelper();
		eventHelper.prepararEventHelper(evento, EventType.VEHICLE_LOCATION, datosLocationJSON);
		
		VehicleLocation vehicleLocation = Helpers.prepararVehicleLocation(eventHelper);
		EventoProcesado eventoProcesado = Helpers.prepararEventoProcesado(eventHelper);
		
		vehicleLocationService.create(vehicleLocation);
		eventService.eliminarEventosProcesados();
		eventService.create(eventoProcesado);
	}

}