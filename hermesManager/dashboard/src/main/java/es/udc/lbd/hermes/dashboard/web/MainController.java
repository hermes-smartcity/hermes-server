package es.udc.lbd.hermes.dashboard.web;

import org.apache.log4j.Logger;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.udc.lbd.hermes.model.util.jackson.CustomGeometryDeserializer;


@Controller
public class MainController {
//	private Logger logger = LoggerFactory.getLogger(MainController.class);
	static Logger logger = Logger.getLogger(MainController.class);
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public void main() {
	}
}

