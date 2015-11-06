package es.enxenio.smart.eventManager.strategy;

import org.json.simple.JSONObject;

import org.springframework.stereotype.Component;


import es.enxenio.smart.model.util.ApplicationContextProvider;
import es.enxenio.smart.model.events.EventType;
import es.enxenio.smart.model.events.dataSection.DataSection;
import es.enxenio.smart.model.events.dataSection.service.DataSectionService;
import es.enxenio.smart.model.events.eventoProcesado.EventoProcesado;
import es.enxenio.smart.model.events.service.EventService;
import es.enxenio.smart.model.util.EventHelper;
import es.enxenio.smart.model.util.Helpers;
import  es.enxenio.smart.eventManager.strategy.EventStrategy;;

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