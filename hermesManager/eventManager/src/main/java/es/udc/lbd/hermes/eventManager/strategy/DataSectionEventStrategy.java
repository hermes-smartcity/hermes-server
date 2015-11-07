package es.udc.lbd.hermes.eventManager.strategy;

import org.json.simple.JSONObject;

import org.springframework.stereotype.Component;

import es.udc.lbd.hermes.model.events.EventType;
import es.udc.lbd.hermes.model.events.dataSection.DataSection;
import es.udc.lbd.hermes.model.events.dataSection.service.DataSectionService;
import es.udc.lbd.hermes.model.events.eventoProcesado.EventoProcesado;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;
import es.udc.lbd.hermes.model.util.EventHelper;
import es.udc.lbd.hermes.model.util.Helpers;


@Component
public class DataSectionEventStrategy extends EventStrategy {

	@Override
	public void processEvent(JSONObject evento) {
		
		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		DataSectionService dataSectionService = ApplicationContextProvider.getApplicationContext().getBean("dataSectionService", DataSectionService.class);

		JSONObject datosBodyJSON = (JSONObject) evento.get("Body");		
		
		EventHelper eventHelper = new EventHelper();
		eventHelper.prepararEventHelper(evento, EventType.DATA_SECTION, datosBodyJSON);
		
		DataSection dataSection = Helpers.prepararDataSection(eventHelper);
		EventoProcesado eventoProcesado = Helpers.prepararEventoProcesado(eventHelper);
				
		dataSectionService.create(dataSection);
		eventService.eliminarEventosProcesados();
		eventService.create(eventoProcesado);
		
	}

}