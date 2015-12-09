package es.udc.lbd.hermes.dashboard.controller.events.dashboard;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.dashboard.controller.util.JSONData;
import es.udc.lbd.hermes.dashboard.web.rest.events.MainResource;
import es.udc.lbd.hermes.eventManager.EventManager;
import es.udc.lbd.hermes.eventManager.util.Helpers;
import es.udc.lbd.hermes.model.events.EventType;
import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.dataSection.service.DataSectionService;
import es.udc.lbd.hermes.model.events.driverFeatures.service.DriverFeaturesService;
import es.udc.lbd.hermes.model.events.eventoProcesado.EventoProcesado;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;
import es.udc.lbd.hermes.model.events.measurement.service.MeasurementService;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.events.vehicleLocation.service.VehicleLocationService;
import es.udc.lbd.hermes.model.usuario.Usuario;
import es.udc.lbd.hermes.model.usuario.service.UsuarioService;


@RestController
@RequestMapping(value = "/api/dashboard")
public class DashboardJSONController extends MainResource {
	private final Logger log = LoggerFactory
			.getLogger(DashboardJSONController.class);

	@Autowired private UsuarioService usuarioService;
	
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

		@RequestMapping(value="/json/usuarios", method = RequestMethod.GET)
		public List<Usuario> getUsuarios() {
			return usuarioService.obterUsuarios();
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
