package es.enxenio.smart.eventManager.strategy;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import es.enxenio.smart.model.events.EventType;
import es.enxenio.smart.model.events.eventoProcesado.EventoProcesado;
import es.enxenio.smart.model.events.service.EventService;
import es.enxenio.smart.model.events.vehicleLocation.VehicleLocation;
import es.enxenio.smart.model.events.vehicleLocation.service.VehicleLocationService;
import es.enxenio.smart.model.util.EventHelper;
import es.enxenio.smart.model.util.Helpers;




public class VehicleLocationEventStrategy extends EventStrategy {

	@Autowired
	private VehicleLocationService vehicleLocationService;
	@Autowired
	private EventService eventService;
	
	@Override
	public void processEvent(JSONObject evento) {

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