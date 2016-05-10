package es.udc.lbd.hermes.eventManager.controller.osmimport.conceptTransformation;

import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.eventManager.controller.util.JSONData;
import es.udc.lbd.hermes.eventManager.util.Helpers;
import es.udc.lbd.hermes.eventManager.web.rest.events.MainResource;
import es.udc.lbd.hermes.model.osmimport.concepttransformation.ConceptTransformation;
import es.udc.lbd.hermes.model.osmimport.concepttransformation.ConceptTransformationDTO;
import es.udc.lbd.hermes.model.osmimport.concepttransformation.service.ConceptTransformationService;

@RestController
@RequestMapping(value = "/api/concepttransformation")
public class ConceptTransformationController extends MainResource{

	static Logger logger = Logger.getLogger(ConceptTransformationController.class);
	
	@Autowired private ConceptTransformationService conceptTransformationServicio;
	
	@Autowired private MessageSource messageSource;
	
	@RequestMapping(value="/json/concepttransformations", method = RequestMethod.GET)
	public List<ConceptTransformation> getConceptTransformations(@RequestParam(value = "idJob", required = true) Long idJob) { 

		return conceptTransformationServicio.getAll(idJob);

	}
	
	@RequestMapping(value = "/delete" + "/{id}", method = RequestMethod.DELETE)
	public JSONData eliminar(@RequestParam(value = "lang", required = false) String lang,
			@PathVariable(value = "id") Long id){
		
		JSONData jsonD = new JSONData();

		conceptTransformationServicio.delete(id);
		
		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("deletedConceptTransformationOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONData register(@RequestBody ConceptTransformationDTO concepttransformation,
			@RequestParam(value = "lang", required = false) String lang) {
		
		JSONData jsonD = new JSONData();
		conceptTransformationServicio.register(concepttransformation);

		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("createdConceptTransformationOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
	
	@RequestMapping(value = "/json/concepttransformation", method = RequestMethod.GET)
	public ConceptTransformation getConceptTransformation(@RequestParam(value = "id", required = true) Long id) {
		return conceptTransformationServicio.get(id);

	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONData update(@PathVariable Long id, @RequestBody ConceptTransformationDTO concepttransformation,
			@RequestParam(value = "lang", required = false) String lang) {
		
		JSONData jsonD = new JSONData();
		conceptTransformationServicio.update(concepttransformation, id);
		
		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("updatedConceptTransformationOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
}
