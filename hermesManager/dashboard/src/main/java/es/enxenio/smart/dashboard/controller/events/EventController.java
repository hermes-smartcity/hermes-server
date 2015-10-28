package es.enxenio.smart.dashboard.controller.events;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.enxenio.smart.dashboard.controller.session.MensaxePendente;
import es.enxenio.smart.dashboard.controller.session.MensaxePendente.TipoMensaxe;
import es.enxenio.smart.dashboard.controller.session.SessionManager;
import es.enxenio.smart.eventManager.EventManager;
import es.enxenio.smart.eventManager.HelperEventManager;
import es.enxenio.smart.model.events.service.EventService;


import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/events")
public class EventController {
	
	@Autowired private ServletContext servletContext;
	
	private static final String EVENT_MANAGER = "EVENT_MANAGER";
	private final Logger log = LoggerFactory.getLogger(EventController.class);
	
	@RequestMapping("/arrancar")
	public String arrancar(HttpSession session) {
		EventManager eventManager = (EventManager) servletContext.getAttribute(EVENT_MANAGER);
		try {
			HelperEventManager.startEventManager(eventManager);
			SessionManager.addMensaxePendente(session, new MensaxePendente("ok.citydriver.arrancando", TipoMensaxe.CORRECTO));
		} /*catch (ArrancarEventManagerException e) {
			SessionManager.addMensaxePendente(session, new MensaxePendente("erros.citydriver.arrancando", TipoMensaxe.ERRO));
			log.error("Excepción arrancando Event Manager ");
		}*/catch (IllegalThreadStateException e) {
			SessionManager.addMensaxePendente(session, new MensaxePendente("erros.citydriver.arrancando", TipoMensaxe.ERRO));
			log.error("Excepción arrancando Event Manager : IllegalThreadStateException");
		}  catch (InterruptedException e) {
			SessionManager.addMensaxePendente(session, new MensaxePendente("erros.citydriver.arrancando", TipoMensaxe.ERRO));
			log.error("Excepción arrancando Event Manager : InterruptedException");
		} 
		return "redirect:/paxinaInicio/inicio";
	}
	
	@RequestMapping("/parar")
	public String parar(HttpSession session) {
		EventManager eventManager = (EventManager) servletContext.getAttribute(EVENT_MANAGER);
		try {
			HelperEventManager.stopEventManager(eventManager);
			SessionManager.addMensaxePendente(session, new MensaxePendente("ok.citydriver.parando", TipoMensaxe.CORRECTO));
		}  catch (IllegalThreadStateException e) {
			SessionManager.addMensaxePendente(session, new MensaxePendente("erros.citydriver.parando", TipoMensaxe.ERRO));
			log.error("Excepción parando Event Manager : IllegalThreadStateException");
		} catch (InterruptedException e) {
			SessionManager.addMensaxePendente(session, new MensaxePendente("erros.citydriver.arrancando", TipoMensaxe.ERRO));
			log.error("Excepción arrancando Event Manager : InterruptedException");
		} 
		return "redirect:/paxinaInicio/inicio";
	}
}
