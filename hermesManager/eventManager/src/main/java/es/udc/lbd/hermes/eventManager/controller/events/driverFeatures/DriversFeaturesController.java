package es.udc.lbd.hermes.eventManager.controller.events.driverFeatures;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.eventManager.EventManager;
import es.udc.lbd.hermes.eventManager.web.rest.MainResource;
import es.udc.lbd.hermes.model.events.driverFeatures.DriverFeatures;
import es.udc.lbd.hermes.model.events.driverFeatures.service.DriverFeaturesService;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.Rol;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.UsuarioWeb;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.service.UsuarioWebService;


@RestController
@RequestMapping(value = "/api/driverFeatures")
public class DriversFeaturesController extends MainResource {
//	private final Logger log = LoggerFactory
//			.getLogger(DriversFeaturesController.class);
	static Logger logger = Logger.getLogger(EventManager.class);
	@Autowired private DriverFeaturesService driverFeaturesServicio;
	@Autowired private UsuarioWebService usuarioWebService;

	@RequestMapping(value="/json/eventsByUsuario", method = RequestMethod.GET)
	public List<DriverFeatures> getDriverFeaturess(@RequestParam(value = "idUsuario", required = true) Long idUsuario) {
		// Un usuario tipo consulta solo puede ver sus propios eventos
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioWeb usuario = (UsuarioWeb) usuarioWebService.loadUserByUsername(auth.getName());
		if(usuario.getRol().equals(Rol.ROLE_CONSULTA)){
			idUsuario = usuario.getUsuarioMovil().getId();			
		}
		return driverFeaturesServicio.obterDriverFeaturessSegunUsuario(idUsuario);

	}
	
	@RequestMapping(value="/json/driversFeatures", method = RequestMethod.GET)
	public List<DriverFeatures> getDriverFeaturess() {
		return driverFeaturesServicio.obterDriverFeaturess();

	}

}
