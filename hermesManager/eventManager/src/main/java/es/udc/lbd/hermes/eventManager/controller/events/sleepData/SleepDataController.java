package es.udc.lbd.hermes.eventManager.controller.events.sleepData;

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
import es.udc.lbd.hermes.model.events.ListaSleepData;
import es.udc.lbd.hermes.model.events.sleepData.service.SleepDataService;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.Rol;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.UsuarioWeb;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.service.UsuarioWebService;

@RestController
@RequestMapping(value = "/api/sleepdata")
public class SleepDataController extends MainResource {

	static Logger logger = Logger.getLogger(SleepDataController.class);

	@Autowired private SleepDataService sleepDataServicio;

	@Autowired private UsuarioWebService usuarioWebService;


	@RequestMapping(value="/json/sleepData", method = RequestMethod.GET)
	public ListaSleepData getSleepData(@RequestParam(value = "idUsuario", required = false) Long idUsuario,		
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
		
		return sleepDataServicio.obterSleepData(idUsuario, ini, fin);

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
		return sleepDataServicio.obterEventosPorDia(idUsuario, ini, fin);		
	}

}
