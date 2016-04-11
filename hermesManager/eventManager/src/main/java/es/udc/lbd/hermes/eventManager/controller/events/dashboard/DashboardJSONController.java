package es.udc.lbd.hermes.eventManager.controller.events.dashboard;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.eventManager.controller.util.JSONData;
import es.udc.lbd.hermes.eventManager.web.rest.MainResource;
import es.udc.lbd.hermes.model.events.EventType;
import es.udc.lbd.hermes.model.events.eventoProcesado.EventoProcesado;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.service.UsuarioMovilService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

@RestController
@RequestMapping(value = "/api/dashboard")
public class DashboardJSONController extends MainResource {
	
	//	private final Logger log = LoggerFactory.getLogger(DashboardJSONController.class);
	static Logger logger = Logger.getLogger(DashboardJSONController.class);

	@Autowired private UsuarioMovilService usuarioMovilService;

	@Autowired private EventService eventService;


	@RequestMapping(value="/json/eventsType", method = RequestMethod.GET)
	public List<EventType> eventsType() {
		return Arrays.asList(EventType.values());
	}

	@RequestMapping(value="/json/measurementTypes", method = RequestMethod.GET)
	public List<MeasurementType> measurementTypes() {
		return Arrays.asList(MeasurementType.values());
	}

	@RequestMapping(value="/json/usuarios", method = RequestMethod.GET)
	public List<UsuarioMovil> getUsuarios() {
		return usuarioMovilService.obterUsuariosMovil();
	}

	@RequestMapping(value="/json/eventsToday", method = RequestMethod.GET)
	public JSONData getEventsToday() {
		JSONData jsonData = new JSONData();
		jsonData.setValueL(eventService.getEventsToday());
		return jsonData;
	}

	@RequestMapping(value="/json/eventoProcesado", method = RequestMethod.GET)
	public EventoProcesado getEventoProcesado() {	
		return eventService.obterEventoProcesado();		
	}
	
	@RequestMapping(value="/json/parametersStatistics", method = RequestMethod.GET)
	public Statistics getParametersStatistics() {
		
		Statistics statistics = ApplicationContextProvider.getApplicationContext().getBean("statisticsComponent", Statistics.class);
	
		return statistics;
	}
}
