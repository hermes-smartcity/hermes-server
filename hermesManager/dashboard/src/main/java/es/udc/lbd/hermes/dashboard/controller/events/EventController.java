package es.udc.lbd.hermes.dashboard.controller.events;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.udc.lbd.hermes.dashboard.controller.session.MensaxePendente;
import es.udc.lbd.hermes.dashboard.controller.session.MensaxePendente.TipoMensaxe;
import es.udc.lbd.hermes.dashboard.controller.session.SessionManager;
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
