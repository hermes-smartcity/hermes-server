package es.enxenio.smart.eventManager.strategy;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.vividsolutions.jts.geom.Geometry;

import es.enxenio.smart.model.events.EventType;
import es.enxenio.smart.model.events.dataSection.DataSection;
import es.enxenio.smart.model.events.dataSection.service.DataSectionService;
import es.enxenio.smart.model.events.eventoProcesado.EventoProcesado;
import es.enxenio.smart.model.events.service.EventService;
import es.enxenio.smart.model.util.EventHelper;
import es.enxenio.smart.model.util.Helpers;

public class DataSectionEventStrategy extends EventStrategy {

	@Autowired
	private DataSectionService dataSectionService;
	@Autowired
	private EventService eventService;

	@Override
	public void processEvent(JSONObject evento) {

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