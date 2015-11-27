package es.udc.lbd.hermes.dashboard.controller.events.dashboard;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import es.udc.lbd.hermes.dashboard.web.rest.events.MainResource;
import es.udc.lbd.hermes.model.events.EventType;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;
import es.udc.lbd.hermes.model.usuario.Usuario;
import es.udc.lbd.hermes.model.usuario.service.UsuarioService;


@RestController
@RequestMapping(value = "/events/dashboard")
public class DashboardJSONController extends MainResource {
	private final Logger log = LoggerFactory
			.getLogger(DashboardJSONController.class);

	
	@Autowired
	private UsuarioService usuarioService;

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
	
}
