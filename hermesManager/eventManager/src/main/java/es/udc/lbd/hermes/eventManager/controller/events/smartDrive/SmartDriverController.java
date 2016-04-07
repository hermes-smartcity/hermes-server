package es.udc.lbd.hermes.eventManager.controller.events.smartDrive;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.model.dataservice.Method;
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
		EnumSet<Method> metodos = Method.smartDriver;
		List<Method> lista = new ArrayList<Method>(metodos.size());
		for(Method metodo: metodos){
			lista.add(metodo);
			
		}
		
		return lista;
	}
		
	@RequestMapping(value="/network/link", method = RequestMethod.GET)
	public NetworkLinkVO getNetworkLine(@RequestParam(value = "currentLong", required = true) Double currentLong,
			@RequestParam(value = "currentLat", required = true) Double currentLat,
			@RequestParam(value = "previousLong", required = true) Double previousLong, 
			@RequestParam(value = "previousLat", required = true) Double previousLat) { 

		return networkServicio.getLinkInformation(currentLong, currentLat, previousLong, previousLat);

	}
}
