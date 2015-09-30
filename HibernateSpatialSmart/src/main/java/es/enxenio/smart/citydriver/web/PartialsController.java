package es.enxenio.smart.citydriver.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/partials")
public class PartialsController {

	@RequestMapping(value = "/{viewName}", method = RequestMethod.GET)
	public void getPartialView() {
	}

}
