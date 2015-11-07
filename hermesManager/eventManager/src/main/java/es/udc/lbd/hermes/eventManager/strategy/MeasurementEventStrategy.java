package es.udc.lbd.hermes.eventManager.strategy;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import es.udc.lbd.hermes.model.events.EventType;
import es.udc.lbd.hermes.model.events.eventoProcesado.EventoProcesado;
import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.service.MeasurementService;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;
import es.udc.lbd.hermes.model.util.EventHelper;
import es.udc.lbd.hermes.model.util.Helpers;



@Component
public class MeasurementEventStrategy extends EventStrategy {
		
	@Override
	public void processEvent(JSONObject evento) {
		
		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		MeasurementService measurementService = ApplicationContextProvider.getApplicationContext().getBean("measurementService", MeasurementService.class);
				
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