package es.udc.lbd.hermes.eventManager.controller.events.hermesServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.eventManager.util.ErrorMessage;
import es.udc.lbd.hermes.model.dataservice.Method;
import es.udc.lbd.hermes.model.dataservice.Service;
import es.udc.lbd.hermes.model.events.dataSection.EnumDataSection;
import es.udc.lbd.hermes.model.smartdriver.AggregateMeasurementVO;
import es.udc.lbd.hermes.model.smartdriver.NetworkLinkVO;
import es.udc.lbd.hermes.model.smartdriver.RouteSegment;
import es.udc.lbd.hermes.model.smartdriver.Type;
import es.udc.lbd.hermes.model.smartdriver.service.NetworkService;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.service.UsuarioWebService;
import es.udc.lbd.hermes.model.util.exceptions.PointDestinyException;
import es.udc.lbd.hermes.model.util.exceptions.PointOriginException;

@RestController
@RequestMapping(value = "/api/hermes")
public class HermesServicesController {

	static Logger logger = Logger.getLogger(HermesServicesController.class);
	
	@Autowired private NetworkService networkServicio;
	
	@Autowired private UsuarioWebService usuarioWebService;
		
	@Autowired private MessageSource messageSource;
	
	@RequestMapping(value="/json/services", method = RequestMethod.GET)
	public List<Service> getServices() {
		return Arrays.asList(Service.values());
	}
	
	@RequestMapping(value = "/json/methods", method = RequestMethod.GET)
	public List<Method> getOperations(@RequestParam(value = "service", required = true) Service service) {
		EnumSet<Method> metodos = Method.smartDriver;
		switch (service) {
		case SMARTDRIVER:
			metodos = Method.smartDriver;
			break;

		case SMARTCITIZEN:
			metodos = Method.smartCitizen;
			break;
		}
				
		List<Method> lista = new ArrayList<Method>(metodos.size());
		for(Method metodo: metodos){
			lista.add(metodo);
			
		}
		
		return lista;
	}

	@RequestMapping(value="/json/types", method = RequestMethod.GET)
	public List<Type> types() {
		return Arrays.asList(Type.values());
	}
	
	@RequestMapping(value="/json/datasections", method = RequestMethod.GET)
	public List<EnumDataSection> datasections() {
		return Arrays.asList(EnumDataSection.values());
	}
	
	@RequestMapping(value="/network/link", method = RequestMethod.GET)
	public NetworkLinkVO getLinkInformation(@RequestParam(value = "currentLong", required = true) Double currentLong,
			@RequestParam(value = "currentLat", required = true) Double currentLat,
			@RequestParam(value = "previousLong", required = true) Double previousLong, 
			@RequestParam(value = "previousLat", required = true) Double previousLat) { 

		return networkServicio.getLinkInformation(currentLong, currentLat, previousLong, previousLat);

	}

	@RequestMapping(value="/measurement/aggregate", method = RequestMethod.GET)
	public AggregateMeasurementVO getAggregateMeasurement(@RequestParam(value = "type", required = true) Type type,
			@RequestParam(value = "lat", required = true) Double lat,
			@RequestParam(value = "lon", required = true) Double lon, 
			@RequestParam(value = "day", required = true) Integer day,
			@RequestParam(value = "time", required = true) Integer time,
			@RequestParam(value = "value", required = false) String value) { 

		return networkServicio.getAggregateMeasurement(type, lat, lon, day, time, value);
	}
	
	@RequestMapping(value="/network/route", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getComputeRoute(@RequestParam(value = "fromLat", required = true) Double fromLat,
			@RequestParam(value = "fromLng", required = true) Double fromLng,
			@RequestParam(value = "toLat", required = true) Double toLat, 
			@RequestParam(value = "toLng", required = true) Double toLng,
			Locale locale) { 

		try{
			List<RouteSegment> lista = networkServicio.getComputeRoute(fromLat, fromLng, toLat, toLng);
		
			return new ResponseEntity<List<RouteSegment>>(lista,HttpStatus.OK);
			
		}catch (PointOriginException e) {
			
			Object [] parametros = new Object[] {fromLat, fromLng, fromLat, fromLng};

			String mensaje = messageSource.getMessage("point.exception", parametros, locale);
			ErrorMessage errorMessage = new ErrorMessage(mensaje);
			
			//El error de debe a que no se encontro punto de origen
			return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.BAD_REQUEST);
			
		}catch (PointDestinyException e) {
			
			Object [] parametros = new Object[] {toLat, toLng, toLat, toLng};

			String mensaje = messageSource.getMessage("point.exception", parametros, locale);
			ErrorMessage errorMessage = new ErrorMessage(mensaje);
			
			//El error de debe a que no se encontro punto de destino
			return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.BAD_REQUEST);
		}

	}
}
