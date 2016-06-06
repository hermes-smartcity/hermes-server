package es.udc.lbd.hermes.eventManager.controller.events.hermesServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.eventManager.util.ErrorMessage;
import es.udc.lbd.hermes.eventManager.util.Helpers;
import es.udc.lbd.hermes.model.dataservice.Method;
import es.udc.lbd.hermes.model.dataservice.Service;
import es.udc.lbd.hermes.model.events.contextData.ContextData;
import es.udc.lbd.hermes.model.events.dataSection.DataSection;
import es.udc.lbd.hermes.model.events.dataSection.EnumDataSection;
import es.udc.lbd.hermes.model.events.driverFeatures.DriverFeatures;
import es.udc.lbd.hermes.model.events.heartRateData.HeartRateData;
import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;
import es.udc.lbd.hermes.model.events.sleepData.SleepData;
import es.udc.lbd.hermes.model.events.stepsData.StepsData;
import es.udc.lbd.hermes.model.events.useractivities.UserActivities;
import es.udc.lbd.hermes.model.events.userlocations.UserLocations;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
import es.udc.lbd.hermes.model.smartdriver.AggregateMeasurementVO;
import es.udc.lbd.hermes.model.smartdriver.NetworkLinkVO;
import es.udc.lbd.hermes.model.smartdriver.RouteSegment;
import es.udc.lbd.hermes.model.smartdriver.RoutePoint;
import es.udc.lbd.hermes.model.smartdriver.Type;
import es.udc.lbd.hermes.model.smartdriver.service.NetworkService;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.UsuarioWeb;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.service.UsuarioWebService;
import es.udc.lbd.hermes.model.util.exceptions.PointDestinyException;
import es.udc.lbd.hermes.model.util.exceptions.PointOriginException;
import es.udc.lbd.hermes.model.util.exceptions.RouteException;

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
	
	@RequestMapping(value="/json/measurementTypes", method = RequestMethod.GET)
	public List<MeasurementType> measurementTypes() {
		return Arrays.asList(MeasurementType.values());
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
			@RequestParam(value = "lang", required = false) String lang) { 

		if (lang == null){
			lang = "en";
		}		
		Locale locale = Helpers.construirLocale(lang);
		try{
			List<RouteSegment> lista = networkServicio.getComputeRoute(fromLat, fromLng, toLat, toLng);
			return new ResponseEntity<List<RouteSegment>>(lista,HttpStatus.OK);
		}catch (PointOriginException e) {
			//El error de debe a que no se encontro punto de origen
			Object [] parametros = new Object[] {fromLat, fromLng, fromLat, fromLng};
			String mensaje = messageSource.getMessage("point.exception", parametros, locale);
			ErrorMessage errorMessage = new ErrorMessage(mensaje);
			return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.BAD_REQUEST);
		}catch (PointDestinyException e) {
			//El error de debe a que no se encontro punto de destino
			Object [] parametros = new Object[] {toLat, toLng, toLat, toLng};
			String mensaje = messageSource.getMessage("point.exception", parametros, locale);
			ErrorMessage errorMessage = new ErrorMessage(mensaje);
			return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.BAD_REQUEST);
		}catch (RouteException e) {
			//El error de debe a que no existe ruta entre el origen y el destino
			String mensaje = messageSource.getMessage("route.exception", null, locale);
			ErrorMessage errorMessage = new ErrorMessage(mensaje);			
			return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value="/network/simulate", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> simulateRoute(@RequestParam(value = "fromLat", required = true) Double fromLat,
			@RequestParam(value = "fromLng", required = true) Double fromLng,
			@RequestParam(value = "toLat", required = true) Double toLat, 
			@RequestParam(value = "toLng", required = true) Double toLng,
			@RequestParam(value = "speedFactor", required = true) Double sf,
			@RequestParam(value = "lang", required = false) String lang) { 

		if (lang == null){
			lang = "en";
		}
		Locale locale = Helpers.construirLocale(lang);
		try{
			List<RoutePoint> lista = networkServicio.simulateRoute(fromLat, fromLng, toLat, toLng, sf);
			return new ResponseEntity<List<RoutePoint>>(lista,HttpStatus.OK);
		}catch (PointOriginException e) {
			//El error de debe a que no se encontro punto de origen
			Object [] parametros = new Object[] {fromLat, fromLng, fromLat, fromLng};
			String mensaje = messageSource.getMessage("point.exception", parametros, locale);
			ErrorMessage errorMessage = new ErrorMessage(mensaje);
			return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.BAD_REQUEST);
		}catch (PointDestinyException e) {
			//El error de debe a que no se encontro punto de destino
			Object [] parametros = new Object[] {toLat, toLng, toLat, toLng};
			String mensaje = messageSource.getMessage("point.exception", parametros, locale);
			ErrorMessage errorMessage = new ErrorMessage(mensaje);
			return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.BAD_REQUEST);
		}catch (RouteException e) {
			//El error de debe a que no se encontro una ruta			
			String mensaje = messageSource.getMessage("route.exception", null, locale);
			ErrorMessage errorMessage = new ErrorMessage(mensaje);
			return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.BAD_REQUEST);
		}
	}	
	
	@RequestMapping(value="/vehiclelocation", method = RequestMethod.GET)
	public List<VehicleLocation> getVehicleLocation(		
			@RequestParam(value = "from", required = true) String from,
			@RequestParam(value = "to", required = true) String to,
			@RequestParam(value = "nwLng", required = true) Double nwLng,
			@RequestParam(value = "nwLat", required = true) Double nwLat,
			@RequestParam(value = "seLng", required = true) Double seLng, 
			@RequestParam(value = "seLat", required = true) Double seLat) { 

		// Un usuario tipo consulta solo puede ver sus propios eventos
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioWeb usuario = (UsuarioWeb) usuarioWebService.loadUserByUsername(auth.getName());
		
		if (usuario.getUsuarioMovil() != null){
			Long idUsuario = usuario.getUsuarioMovil().getId();     
			
			Calendar ini = Helpers.getFecha(from);
			Calendar fin = Helpers.getFecha(to);
			
			return networkServicio.getVehicleLocation(idUsuario, ini, fin,
				nwLng, nwLat,seLng, seLat);
		}else{
			return new ArrayList<VehicleLocation>();
		}

	}
	
	@RequestMapping(value="/measurement", method = RequestMethod.GET)
	public List<Measurement> getMeasurement(@RequestParam(required = true) MeasurementType tipo,	
			@RequestParam(value = "from", required = true) String from,
			@RequestParam(value = "to", required = true) String to,
			@RequestParam(value = "nwLng", required = true) Double nwLng,
			@RequestParam(value = "nwLat", required = true) Double nwLat,
			@RequestParam(value = "seLng", required = true) Double seLng, 
			@RequestParam(value = "seLat", required = true) Double seLat) { 

		// Un usuario tipo consulta solo puede ver sus propios eventos
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioWeb usuario = (UsuarioWeb) usuarioWebService.loadUserByUsername(auth.getName());
		if (usuario.getUsuarioMovil() != null){
			Long idUsuario = usuario.getUsuarioMovil().getId();     
			
			Calendar ini = Helpers.getFecha(from);
			Calendar fin = Helpers.getFecha(to);
			
			return networkServicio.getMeasurement(tipo, idUsuario, ini, fin,
					nwLng, nwLat,seLng, seLat);
		}else{
			return new ArrayList<Measurement>();
		}
	}
	
	@RequestMapping(value="/datasection", method = RequestMethod.GET)
	public List<DataSection> getDataSection(		
			@RequestParam(value = "from", required = true) String from,
			@RequestParam(value = "to", required = true) String to,
			@RequestParam(value = "nwLng", required = true) Double nwLng,
			@RequestParam(value = "nwLat", required = true) Double nwLat,
			@RequestParam(value = "seLng", required = true) Double seLng, 
			@RequestParam(value = "seLat", required = true) Double seLat) { 

		// Un usuario tipo consulta solo puede ver sus propios eventos
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioWeb usuario = (UsuarioWeb) usuarioWebService.loadUserByUsername(auth.getName());
		if (usuario.getUsuarioMovil() != null){
			Long idUsuario = usuario.getUsuarioMovil().getId();     
			
			Calendar ini = Helpers.getFecha(from);
			Calendar fin = Helpers.getFecha(to);
			
			return networkServicio.getDataSection(idUsuario, ini, fin,
					nwLng, nwLat,seLng, seLat);
		}else{
			return new ArrayList<DataSection>();
		}
	}
	
	@RequestMapping(value="/driverfeatures", method = RequestMethod.GET)
	public List<DriverFeatures> getDrivesFeatures(		
			@RequestParam(value = "from", required = true) String from,
			@RequestParam(value = "to", required = true) String to) { 

		// Un usuario tipo consulta solo puede ver sus propios eventos
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioWeb usuario = (UsuarioWeb) usuarioWebService.loadUserByUsername(auth.getName());
		if (usuario.getUsuarioMovil() != null){
			Long idUsuario = usuario.getUsuarioMovil().getId();     
			
			Calendar ini = Helpers.getFecha(from);
			Calendar fin = Helpers.getFecha(to);
			
			return networkServicio.getDriverFeatures(idUsuario, ini, fin);
		}else{
			return new ArrayList<DriverFeatures>();
		}
	}
	
	@RequestMapping(value="/heartratedata", method = RequestMethod.GET)
	public List<HeartRateData> getHeartRateData(		
			@RequestParam(value = "from", required = true) String from,
			@RequestParam(value = "to", required = true) String to) { 

		// Un usuario tipo consulta solo puede ver sus propios eventos
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioWeb usuario = (UsuarioWeb) usuarioWebService.loadUserByUsername(auth.getName());
		if (usuario.getUsuarioMovil() != null){
			Long idUsuario = usuario.getUsuarioMovil().getId();  
			
			Calendar ini = Helpers.getFecha(from);
			Calendar fin = Helpers.getFecha(to);
			
			return networkServicio.getHeartRateData(idUsuario, ini, fin);
		}else{
			return new ArrayList<HeartRateData>();
		}
	}
	
	@RequestMapping(value="/stepsdata", method = RequestMethod.GET)
	public List<StepsData> getStepsData(		
			@RequestParam(value = "from", required = true) String from,
			@RequestParam(value = "to", required = true) String to) { 

		// Un usuario tipo consulta solo puede ver sus propios eventos
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioWeb usuario = (UsuarioWeb) usuarioWebService.loadUserByUsername(auth.getName());
		if (usuario.getUsuarioMovil() != null){
			Long idUsuario = usuario.getUsuarioMovil().getId();  
			
			Calendar ini = Helpers.getFecha(from);
			Calendar fin = Helpers.getFecha(to);
			
			return networkServicio.getStepsData(idUsuario, ini, fin);
		}else{
			return new ArrayList<StepsData>();
		}
	}
	
	@RequestMapping(value="/sleepdata", method = RequestMethod.GET)
	public List<SleepData> getSleepData(		
			@RequestParam(value = "from", required = true) String from,
			@RequestParam(value = "to", required = true) String to) { 

		// Un usuario tipo consulta solo puede ver sus propios eventos
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioWeb usuario = (UsuarioWeb) usuarioWebService.loadUserByUsername(auth.getName());
		if (usuario.getUsuarioMovil() != null){
			Long idUsuario = usuario.getUsuarioMovil().getId();  
			
			Calendar ini = Helpers.getFecha(from);
			Calendar fin = Helpers.getFecha(to);
			
			return networkServicio.getSleepData(idUsuario, ini, fin);
		}else{
			return new ArrayList<SleepData>();
		}
	}
	
	@RequestMapping(value="/contextdata", method = RequestMethod.GET)
	public List<ContextData> getContextData(		
			@RequestParam(value = "from", required = true) String from,
			@RequestParam(value = "to", required = true) String to,
			@RequestParam(value = "nwLng", required = true) Double nwLng,
			@RequestParam(value = "nwLat", required = true) Double nwLat,
			@RequestParam(value = "seLng", required = true) Double seLng, 
			@RequestParam(value = "seLat", required = true) Double seLat) { 

		// Un usuario tipo consulta solo puede ver sus propios eventos
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioWeb usuario = (UsuarioWeb) usuarioWebService.loadUserByUsername(auth.getName());
		if (usuario.getUsuarioMovil() != null){
			Long idUsuario = usuario.getUsuarioMovil().getId();     
			
			Calendar ini = Helpers.getFecha(from);
			Calendar fin = Helpers.getFecha(to);
			
			return networkServicio.getContextData(idUsuario, ini, fin,
					nwLng, nwLat,seLng, seLat);
		}else{
			return new ArrayList<ContextData>();
		}
	}
	
	@RequestMapping(value="/userlocations", method = RequestMethod.GET)
	public List<UserLocations> getUserLocations(		
			@RequestParam(value = "from", required = true) String from,
			@RequestParam(value = "to", required = true) String to,
			@RequestParam(value = "nwLng", required = true) Double nwLng,
			@RequestParam(value = "nwLat", required = true) Double nwLat,
			@RequestParam(value = "seLng", required = true) Double seLng, 
			@RequestParam(value = "seLat", required = true) Double seLat) { 

		// Un usuario tipo consulta solo puede ver sus propios eventos
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioWeb usuario = (UsuarioWeb) usuarioWebService.loadUserByUsername(auth.getName());
		if (usuario.getUsuarioMovil() != null){
			Long idUsuario = usuario.getUsuarioMovil().getId();     
			
			Calendar ini = Helpers.getFecha(from);
			Calendar fin = Helpers.getFecha(to);
			
			return networkServicio.getUserLocations(idUsuario, ini, fin,
					nwLng, nwLat,seLng, seLat);
		}else{
			return new ArrayList<UserLocations>();
		}
	}
	
	@RequestMapping(value="/useractivities", method = RequestMethod.GET)
	public List<UserActivities> getUserActivities(		
			@RequestParam(value = "from", required = true) String from,
			@RequestParam(value = "to", required = true) String to) { 

		// Un usuario tipo consulta solo puede ver sus propios eventos
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioWeb usuario = (UsuarioWeb) usuarioWebService.loadUserByUsername(auth.getName());
		if (usuario.getUsuarioMovil() != null){
			Long idUsuario = usuario.getUsuarioMovil().getId();  
			
			Calendar ini = Helpers.getFecha(from);
			Calendar fin = Helpers.getFecha(to);
			
			return networkServicio.getUserActivities(idUsuario, ini, fin);
		}else{
			return new ArrayList<UserActivities>();
		}
	}
}
