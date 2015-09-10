package es.enxenio.smart.citydriver.strategy;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import es.enxenio.smart.citydriver.model.events.EventType;
import es.enxenio.smart.citydriver.model.events.eventoProcesado.EventoProcesado;
import es.enxenio.smart.citydriver.model.events.service.EventService;
import es.enxenio.smart.citydriver.model.events.vehicleLocation.VehicleLocation;
import es.enxenio.smart.citydriver.model.events.vehicleLocation.service.VehicleLocationService;
import es.enxenio.smart.citydriver.model.util.EventHelper;
import es.enxenio.smart.citydriver.model.util.Helpers;

public class VehicleLocationEventStrategy extends EventStrategy {

	@Autowired
	private VehicleLocationService vehicleLocationService;
	ApplicationContext context = new FileSystemXmlApplicationContext("src\\main\\java\\es\\enxenio\\smart\\citydriver\\spring-config.xml");
	
	@Override
	public void processEvent(JSONObject evento) {
		EventService eventService = (EventService) context.getBean("eventService");
		VehicleLocationService vehicleLocationService = (VehicleLocationService) context.getBean("vehicleLocationService");
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