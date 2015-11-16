package es.udc.lbd.hermes.dashboard.controller.events;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.udc.lbd.hermes.eventManager.EventManager;
import es.udc.lbd.hermes.eventManager.HelperEventManager;


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
		} /*catch (ArrancarEventManagerException e) {
			SessionManager.addMensaxePendente(session, new MensaxePendente("erros.citydriver.arrancando", TipoMensaxe.ERRO));
			log.error("Excepción arrancando Event Manager ");
		}*/catch (IllegalThreadStateException e) {
			log.error("Excepción arrancando Event Manager : IllegalThreadStateException");
		}  catch (InterruptedException e) {
			log.error("Excepción arrancando Event Manager : InterruptedException");
		} 
		return "redirect:/paxinaInicio/inicio";
	}
	
	@RequestMapping("/parar")
	public String parar(HttpSession session) {
		EventManager eventManager = (EventManager) servletContext.getAttribute(EVENT_MANAGER);
		try {
			HelperEventManager.stopEventManager(eventManager);
		}  catch (IllegalThreadStateException e) {
			log.error("Excepción parando Event Manager : IllegalThreadStateException");
		} catch (InterruptedException e) {
			log.error("Excepción arrancando Event Manager : InterruptedException");
		} 
		return "redirect:/paxinaInicio/inicio";
	}
}
