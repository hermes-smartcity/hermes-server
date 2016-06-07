package es.udc.lbd.hermes.eventManager.controller.events.userSleep;

import java.util.Calendar;

import org.apache.log4j.Logger;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.eventManager.util.Helpers;
import es.udc.lbd.hermes.eventManager.web.rest.MainResource;
import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.ListaUserSleep;
import es.udc.lbd.hermes.model.events.usersleep.service.UserSleepService;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.Rol;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.UsuarioWeb;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.service.UsuarioWebService;

@RestController
@RequestMapping(value = "/api/usersleep")
public class UserSleepController extends MainResource {
	//	private final Logger log = LoggerFactory
	//			.getLogger(VehicleLocationsController.class);
	static Logger logger = Logger.getLogger(UserSleepController.class);

	@Autowired private UserSleepService userSleepServicio;

	@Autowired private UsuarioWebService usuarioWebService;


	@RequestMapping(value="/json/userSleep", method = RequestMethod.GET)
	public ListaUserSleep getUserSleep(@RequestParam(value = "idUsuario", required = false) Long idUsuario,		
			@RequestParam(value = "fechaIni", required = true) String fechaIni,
			@RequestParam(value = "fechaFin", required = true) String fechaFin) { 

		// Un usuario tipo consulta solo puede ver sus propios eventos
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioWeb usuario = (UsuarioWeb) usuarioWebService.loadUserByUsername(auth.getName());
		if(usuario.getRol().equals(Rol.ROLE_CONSULTA)){
			idUsuario = usuario.getUsuarioMovil().getId();			
		}
		Calendar ini = Helpers.getFecha(fechaIni);
		Calendar fin = Helpers.getFecha(fechaFin);
		
		return userSleepServicio.obterUserSleep(idUsuario, ini, fin);

	}

	@RequestMapping(value="/json/eventosPorDia", method = RequestMethod.GET)
	public ListaEventosYdias getEventosPorDia(@RequestParam(value = "idUsuario", required = false) Long idUsuario,		
			@RequestParam(value = "fechaIni", required = true) String fechaIni,
			@RequestParam(value = "fechaFin", required = true) String fechaFin) {

		// Un usuario tipo consulta solo puede ver sus propios eventos
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioWeb usuario = (UsuarioWeb) usuarioWebService.loadUserByUsername(auth.getName());
		if(usuario.getRol().equals(Rol.ROLE_CONSULTA)){
			idUsuario = usuario.getUsuarioMovil().getId();			
		}
		Calendar ini = Helpers.getFecha(fechaIni);
		Calendar fin = Helpers.getFecha(fechaFin);
		return userSleepServicio.obterEventosPorDia(idUsuario, ini, fin);		
	}

}
