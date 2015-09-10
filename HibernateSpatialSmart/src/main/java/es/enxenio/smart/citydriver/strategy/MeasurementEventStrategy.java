package es.enxenio.smart.citydriver.strategy;

import org.json.simple.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import es.enxenio.smart.citydriver.model.events.EventType;
import es.enxenio.smart.citydriver.model.events.eventoProcesado.EventoProcesado;
import es.enxenio.smart.citydriver.model.events.measurement.Measurement;
import es.enxenio.smart.citydriver.model.events.measurement.service.MeasurementService;
import es.enxenio.smart.citydriver.model.events.service.EventService;
import es.enxenio.smart.citydriver.model.util.EventHelper;
import es.enxenio.smart.citydriver.model.util.Helpers;

public class MeasurementEventStrategy extends EventStrategy {

	ApplicationContext context = new FileSystemXmlApplicationContext("src\\main\\java\\es\\enxenio\\smart\\citydriver\\spring-config.xml");
	
	@Override
	public void processEvent(JSONObject evento) {
		MeasurementService measurementService = (MeasurementService) context.getBean("measurementService");
		EventService eventService = (EventService) context.getBean("eventService");
		
		EventType tipoEvento = EventType.getTipo((String) evento.get("Event-Type"));
	
		JSONObject datosBodyJSON = (JSONObject) evento.get("Body");
		
		String param = Helpers.prepararParametros(tipoEvento);
		
		JSONObject datosLocationJSON = (JSONObject) datosBodyJSON.get(param);
		EventHelper eventHelper = new EventHelper();
		
		eventHelper.prepararEventHelper(evento, tipoEvento, datosLocationJSON);
		
		Measurement measurement = Helpers.prepararMeasurement(eventHelper);
		EventoProcesado eventoProcesado = Helpers.prepararEventoProcesado(eventHelper);
		
		measurementService.create(measurement);
		eventService.eliminarEventosProcesados();
		eventService.create(eventoProcesado);
	}

}