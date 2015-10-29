package es.enxenio.smart.eventManager.strategy;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import es.enxenio.smart.model.events.EventType;
import es.enxenio.smart.model.events.eventoProcesado.EventoProcesado;
import es.enxenio.smart.model.events.measurement.Measurement;
import es.enxenio.smart.model.events.measurement.service.MeasurementService;
import es.enxenio.smart.model.events.service.EventService;
import es.enxenio.smart.model.util.EventHelper;
import es.enxenio.smart.model.util.Helpers;


public class MeasurementEventStrategy extends EventStrategy {

	@Autowired
	private MeasurementService measurementService;
	@Autowired
	private EventService eventService;
	
	@Override
	public void processEvent(JSONObject evento) {
		
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