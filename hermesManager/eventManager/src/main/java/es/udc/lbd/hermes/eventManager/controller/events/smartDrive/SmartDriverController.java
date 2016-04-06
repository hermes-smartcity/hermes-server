package es.udc.lbd.hermes.eventManager.controller.events.smartDrive;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.model.smartdriver.Method;
import es.udc.lbd.hermes.model.smartdriver.NetworkLinkVO;
import es.udc.lbd.hermes.model.smartdriver.service.NetworkService;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.service.UsuarioWebService;

@RestController
@RequestMapping(value = "/api/smartdriver")
public class SmartDriverController {

	static Logger logger = Logger.getLogger(SmartDriverController.class);
	
	@Autowired private NetworkService networkServicio;
	
	@Autowired private UsuarioWebService usuarioWebService;
	
	@RequestMapping(value="/json/methods", method = RequestMethod.GET)
	public List<Method> methods() {
		return Arrays.asList(Method.values());
	}
	
	@RequestMapping(value="/network/link", method = RequestMethod.GET, params = {"c", "p"})
	public NetworkLinkVO getLinkInformation(@RequestParam String c, @RequestParam String p) { 

		String[] current = c.split(",");
		if (current.length==2) {
			String[] previous = c.split(",");
			if (previous.length==2) {
				Double currentLat = Double.parseDouble(current[0]);
				Double currentLong = Double.parseDouble(current[1]);
				Double previousLat = Double.parseDouble(previous[0]);
				Double previousLong = Double.parseDouble(previous[1]);
				return networkServicio.getLinkInformation(currentLat, currentLong, previousLat, previousLong);
			}
		}
		return null;
	}
}
