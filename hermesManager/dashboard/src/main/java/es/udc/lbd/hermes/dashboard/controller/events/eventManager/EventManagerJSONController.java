package es.udc.lbd.hermes.dashboard.controller.events.eventManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.dashboard.controller.util.JSONData;
import es.udc.lbd.hermes.dashboard.web.rest.events.MainResource;
import es.udc.lbd.hermes.eventManager.EventManager;

@RestController
@RequestMapping(value = "/api/eventManager")
public class EventManagerJSONController extends MainResource {
	private final Logger log = LoggerFactory
			.getLogger(EventManager.class);

	
	@Autowired private EventManager eventManager;
	
	// TODO falta decidir si es la mejor opcion un post ?¿
	@RequestMapping(value = "/arrancar", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void arrancar(){		
		eventManager.startEventProcessor();
	}
	
	// TODO falta decidir si es la mejor opcion un post ?¿
	@RequestMapping(value = "/parar", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void parar(){		
		eventManager.stopEventProcessor();
	}
	
	//TODO mover a eventManager
	@RequestMapping(value="/json/stateEventManager", method = RequestMethod.GET)
	public JSONData getStateEventManager() {
		JSONData jsonData = new JSONData();
		// Parado
		if (eventManager.getEventProcessor() == null)			
			jsonData.setValue("Stopped");			
		else jsonData.setValue("Running");
		
		return jsonData;
	}
	
}
