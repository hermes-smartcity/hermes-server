package es.enxenio.smart.citydriver.strategy;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.vividsolutions.jts.geom.Geometry;

import es.enxenio.smart.citydriver.model.events.EventType;
import es.enxenio.smart.citydriver.model.events.dataSection.DataSection;
import es.enxenio.smart.citydriver.model.events.dataSection.service.DataSectionService;
import es.enxenio.smart.citydriver.model.events.eventoProcesado.EventoProcesado;
import es.enxenio.smart.citydriver.model.events.service.EventService;
import es.enxenio.smart.citydriver.model.util.EventHelper;
import es.enxenio.smart.citydriver.model.util.Helpers;

public class DataSectionEventStrategy extends EventStrategy {

	@Autowired
	private DataSectionService dataSectionService;
	ApplicationContext context = new FileSystemXmlApplicationContext("src\\main\\java\\es\\enxenio\\smart\\citydriver\\spring-config.xml");
	
	@Override
	public void processEvent(JSONObject evento) {
		DataSectionService dataSectionService = (DataSectionService) context.getBean("dataSectionService");
		EventService eventService = (EventService) context.getBean("eventService");
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