package es.udc.lbd.hermes.dashboard.controller.events.vehicleLocation;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import es.udc.lbd.hermes.dashboard.web.rest.events.MainResource;
import es.udc.lbd.hermes.eventManager.util.Helpers;
import es.udc.lbd.hermes.model.events.EventType;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
import es.udc.lbd.hermes.model.events.vehicleLocation.service.VehicleLocationService;
import es.udc.lbd.hermes.model.usuario.Usuario;
import es.udc.lbd.hermes.model.usuario.service.UsuarioService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping(value = "/events/vehiclelocation")
public class VehicleLocationsController extends MainResource {
	private final Logger log = LoggerFactory
			.getLogger(VehicleLocationsController.class);

	@Autowired
	private VehicleLocationService vehicleLocationServicio;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private UsuarioService usuarioService;

//	@RequestMapping(value="/json/eventsByUsuario", method = RequestMethod.GET)
//	public List<VehicleLocation> getVehicleLocations(@RequestParam(value = "idUsuario", required = true) Long idUsuario) {
//		System.out.println(" --- "+vehicleLocationServicio.obterVehicleLocationsSegunUsuario(idUsuario).size());
//		return vehicleLocationServicio.obterVehicleLocationsSegunUsuario(idUsuario);
//
//	}
	
	@RequestMapping(value="/json/vehicleLocations", method = RequestMethod.GET)
	public List<VehicleLocation> getVehicleLocations(@RequestParam(value = "idUsuario", required = false) Long idUsuario,		
			@RequestParam(value = "fechaIni", required = false) String fechaIni,
			@RequestParam(value = "fechaFin", required = false) String fechaFin,
			@RequestParam(value = "wnLng", required = true) Double wnLng,
			@RequestParam(value = "wnLat", required = true) Double wnLat,
			@RequestParam(value = "esLng", required = true) Double esLng, 
			@RequestParam(value = "esLat", required = true) Double esLat) { 

			Calendar ini = Helpers.getFecha(fechaIni);
			Calendar fin = Helpers.getFecha(fechaFin);
		
			return vehicleLocationServicio.obterVehicleLocations(idUsuario, ini, fin,
					wnLng, wnLat,esLng, esLat);
		
	}
	
//	@RequestMapping(value="/json/vehicleLocationsByBounds", method = RequestMethod.GET)
//	public List<VehicleLocation> getVehicleLocationsByBounds(
//			@RequestParam(value = "wnLng", required = true) Double wnLng,
//			@RequestParam(value = "wnLat", required = true) Double wnLat,
//			@RequestParam(value = "esLng", required = true) Double esLng, 
//			@RequestParam(value = "esLat", required = true) Double esLat) {
//		System.out.println(" -- - -- "+vehicleLocationServicio.obterVehicleLocationsByBounds(wnLng, wnLat, esLng, esLat).size());
//		return vehicleLocationServicio.obterVehicleLocationsByBounds(wnLng, wnLat, esLng, esLat);
//
//	}
	
		//TODO Luego habrá que cambiarlo de ubicación, a otra carpeta
		@RequestMapping(value="/json/eventsType", method = RequestMethod.GET)
		public List<EventType> eventsType() {
			return Arrays.asList(EventType.values());

		}

		//TODO Cambiar de ubicacion
		@RequestMapping(value="/json/usuarios", method = RequestMethod.GET)
		public List<Usuario> getUsuarios() {
			return usuarioService.obterUsuarios();

		}
	
}
