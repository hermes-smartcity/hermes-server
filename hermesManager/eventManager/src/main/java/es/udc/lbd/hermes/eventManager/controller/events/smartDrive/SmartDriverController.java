package es.udc.lbd.hermes.eventManager.controller.events.smartDrive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.model.dataservice.Method;
import es.udc.lbd.hermes.model.events.dataSection.EnumDataSection;
import es.udc.lbd.hermes.model.smartdriver.AggregateMeasurementVO;
import es.udc.lbd.hermes.model.smartdriver.NetworkLinkVO;
import es.udc.lbd.hermes.model.smartdriver.Type;
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
	public NetworkLinkVO getLinkInformation(@RequestParam(value = "currentLong", required = true) Double currentLong,
			@RequestParam(value = "currentLat", required = true) Double currentLat,
			@RequestParam(value = "previousLong", required = true) Double previousLong, 
			@RequestParam(value = "previousLat", required = true) Double previousLat) { 

		return networkServicio.getLinkInformation(currentLong, currentLat, previousLong, previousLat);

	}
	
	@RequestMapping(value="/json/types", method = RequestMethod.GET)
	public List<Type> types() {
		return Arrays.asList(Type.values());
	}
	
	@RequestMapping(value="/json/datasections", method = RequestMethod.GET)
	public List<EnumDataSection> datasections() {
		return Arrays.asList(EnumDataSection.values());
	}
	
	@RequestMapping(value="/measurement/aggregate", method = RequestMethod.GET)
	public AggregateMeasurementVO getAggregateMeasurement(@RequestParam(value = "type", required = true) Type type,
			@RequestParam(value = "lat", required = true) Double lat,
			@RequestParam(value = "lon", required = true) Double lon, 
			@RequestParam(value = "day", required = true) Integer day,
			@RequestParam(value = "time", required = true) Integer time,
			@RequestParam(value = "campo", required = false) String campo) { 

		return networkServicio.getAggregateMeasurement(type, lat, lon, day, time, campo);
	}
}
