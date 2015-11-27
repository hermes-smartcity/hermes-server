package es.udc.lbd.hermes.dashboard.controller.events;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.udc.lbd.hermes.eventManager.EventManager;


@Controller
@RequestMapping("/events")
public class EventController {
	
	@Autowired private EventManager eventManager;
	
	@RequestMapping("/arrancar")
	public String arrancar(HttpSession session) {
		eventManager.startEventProcessor();
		return "redirect:/events/eventManager/mostrar";
	}
	
	@RequestMapping("/parar")
	public String parar(HttpSession session) {		
		eventManager.stopEventProcessor();
		return "redirect:/events/eventManager/mostrar";
	}
}
