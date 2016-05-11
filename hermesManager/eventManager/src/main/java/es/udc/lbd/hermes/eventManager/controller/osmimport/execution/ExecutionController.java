package es.udc.lbd.hermes.eventManager.controller.osmimport.execution;

import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.eventManager.controller.util.JSONData;
import es.udc.lbd.hermes.eventManager.util.Helpers;
import es.udc.lbd.hermes.eventManager.web.rest.MainResource;
import es.udc.lbd.hermes.model.osmimport.execution.Execution;
import es.udc.lbd.hermes.model.osmimport.execution.service.ExecutionService;

@RestController
@RequestMapping(value = "/api/execution")
public class ExecutionController extends MainResource{

	static Logger logger = Logger.getLogger(ExecutionController.class);
	
	@Autowired private ExecutionService executionServicio;
	
	@Autowired private MessageSource messageSource;
	
	@RequestMapping(value="/json/executions", method = RequestMethod.GET)
	public List<Execution> getExecutions() { 

		return executionServicio.getAll();

	}
	
	@RequestMapping(value = "/delete" + "/{id}", method = RequestMethod.DELETE)
	public JSONData eliminar(@RequestParam(value = "lang", required = false) String lang,
			@PathVariable(value = "id") Long id){
		
		JSONData jsonD = new JSONData();

		executionServicio.delete(id);
		
		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("deletedExecutionOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
}
