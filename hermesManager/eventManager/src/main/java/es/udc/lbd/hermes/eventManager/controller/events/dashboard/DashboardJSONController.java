package es.udc.lbd.hermes.eventManager.controller.events.dashboard;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.eventManager.controller.util.JSONData;
import es.udc.lbd.hermes.eventManager.web.rest.MainResource;
import es.udc.lbd.hermes.model.events.EventType;

import es.udc.lbd.hermes.model.events.dataSection.service.DataSectionService;
import es.udc.lbd.hermes.model.events.driverFeatures.service.DriverFeaturesService;
import es.udc.lbd.hermes.model.events.eventoProcesado.EventoProcesado;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;
import es.udc.lbd.hermes.model.events.measurement.service.MeasurementService;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.events.vehicleLocation.service.VehicleLocationService;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.UsuarioMovil;
import es.udc.lbd.hermes.model.usuario.usuarioMovil.service.UsuarioMovilService;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.service.UsuarioWebService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/dashboard")
public class DashboardJSONController extends MainResource {
//	private final Logger log = LoggerFactory.getLogger(DashboardJSONController.class);
	static Logger logger = Logger.getLogger(DashboardJSONController.class);
	
	@Autowired private UsuarioMovilService usuarioMovilService;
	
	@Autowired private UsuarioWebService usuarioWebService;
	
	@Autowired private EventService eventService;
	
	@Autowired private VehicleLocationService vehicleLocationService;
	
	@Autowired private DataSectionService dataSectionService;
	
	@Autowired private MeasurementService measurementService;
	
	@Autowired private DriverFeaturesService driverFeaturesService;

		@RequestMapping(value="/json/eventsType", method = RequestMethod.GET)
		public List<EventType> eventsType() {
			return Arrays.asList(EventType.values());
		}
		
		@RequestMapping(value="/json/measurementTypes", method = RequestMethod.GET)
		public List<MeasurementType> measurementTypes() {
			return Arrays.asList(MeasurementType.values());
		}

		@RequestMapping(value="/json/contarUsuariosMovil", method = RequestMethod.GET)
		public JSONData getContarUsuariosMovil() {
			JSONData jsonData = new JSONData();
			jsonData.setValueL(usuarioMovilService.contar());
			return jsonData;
		}
		
		@RequestMapping(value="/json/contarUsuariosWeb", method = RequestMethod.GET)
		public JSONData getContarUsuariosWeb() {
			JSONData jsonData = new JSONData();
			jsonData.setValueL(usuarioWebService.contar());
			return jsonData;
		}
		
		@RequestMapping(value="/json/numberActiveUsers", method = RequestMethod.GET)
		public JSONData getNumberActiveUsers() {
			JSONData jsonData = new JSONData();
			jsonData.setValueL(usuarioMovilService.getNumberActiveUsers());
			return jsonData;
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
	
		@RequestMapping(value="/json/totalVLocations", method = RequestMethod.GET)
		public JSONData getTotalVLocations() {
			JSONData jsonData = new JSONData();
			jsonData.setValueL(vehicleLocationService.contar());
			return jsonData;
		}
		
		@RequestMapping(value="/json/totalDataScts", method = RequestMethod.GET)
		public JSONData getTotalDataScts() {
			JSONData jsonData = new JSONData();
			jsonData.setValueL(dataSectionService.contar());
			return jsonData;
		}
		
		@RequestMapping(value="/json/totalMeasurements", method = RequestMethod.GET)
		public JSONData getTotalMeasurements() {
			JSONData jsonData = new JSONData();
			jsonData.setValueL(measurementService.contar());
			return jsonData;
		}
		
		@RequestMapping(value="/json/totalDriversF", method = RequestMethod.GET)
		public JSONData getTotalDriversF() {
			JSONData jsonData = new JSONData();
			jsonData.setValueL(driverFeaturesService.contar());
			return jsonData;
		}
	}
