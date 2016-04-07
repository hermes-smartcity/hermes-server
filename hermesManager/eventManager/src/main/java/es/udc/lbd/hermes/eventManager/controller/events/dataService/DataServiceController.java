package es.udc.lbd.hermes.eventManager.controller.events.dataService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.eventManager.util.Helpers;
import es.udc.lbd.hermes.model.dataservice.Method;
import es.udc.lbd.hermes.model.dataservice.Service;
import es.udc.lbd.hermes.model.dataservice.service.DataServicesService;
import es.udc.lbd.hermes.model.events.ListaEventosYdias;

@RestController
@RequestMapping(value = "/api/dataservice")
public class DataServiceController {

	static Logger logger = Logger.getLogger(DataServiceController.class);
	
	@Autowired private DataServicesService dataServiceServicio;
	
	@RequestMapping(value="/json/services", method = RequestMethod.GET)
	public List<Service> getServices() {
		return Arrays.asList(Service.values());
	}
	
	@RequestMapping(value = "/json/operations", method = RequestMethod.GET)
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
	
	@RequestMapping(value="/json/peticionesPorDia", method = RequestMethod.GET)
	public ListaEventosYdias getPeticionesPorDia(@RequestParam(value = "service", required = false) String service,		
			@RequestParam(value = "method", required = false) String method,
			@RequestParam(value = "fechaIni", required = true) String fechaIni,
			@RequestParam(value = "fechaFin", required = true) String fechaFin) {
		
		Calendar ini = Helpers.getFecha(fechaIni);
		Calendar fin = Helpers.getFecha(fechaFin);
		return dataServiceServicio.obterPeticionesPorDia(service, method, ini, fin);		
	}
}
