package es.udc.lbd.hermes.eventManager.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/partials/events")
public class PartialsController {

	@RequestMapping(value = "/{viewName}", method = RequestMethod.GET)
	public void getPartialView() {
	}

}