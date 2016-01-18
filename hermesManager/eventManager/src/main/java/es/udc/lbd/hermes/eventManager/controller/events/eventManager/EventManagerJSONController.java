package es.udc.lbd.hermes.eventManager.controller.events.eventManager;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.eventManager.EventManager;
import es.udc.lbd.hermes.eventManager.controller.util.JSONData;
import es.udc.lbd.hermes.eventManager.web.rest.MainResource;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/eventManager")
public class EventManagerJSONController extends MainResource {
//	private final Logger log = LoggerFactory
//			.getLogger(EventManager.class);

	
	@Autowired private EventManager eventManager;
	
	@RequestMapping(value = "/arrancar", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void arrancar(){		
		eventManager.startEventProcessor();
	}
	
	@RequestMapping(value = "/parar", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void parar(){		
		eventManager.stopEventProcessor();
	}
	
	@RequestMapping(value="/json/stateEventManager", method = RequestMethod.GET)
	public JSONData getStateEventManager() {
		JSONData jsonData = new JSONData();
		// Parado
		if (eventManager.getEventProcessor() == null)			
			jsonData.setValue("Stopped");			
		else jsonData.setValue("Running");
		
		return jsonData;
	}
	
	@RequestMapping(value="/json/user", method = RequestMethod.GET)
	public JSONData getUser() {
		return null;
		// Modo chapuza, llamare a una funcion de user service donde recupere el usuario con ese password y ese login
		// Luego aplicarlo con spring-security
	}
	
}
