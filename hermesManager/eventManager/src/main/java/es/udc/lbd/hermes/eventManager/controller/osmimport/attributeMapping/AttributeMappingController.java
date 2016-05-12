package es.udc.lbd.hermes.eventManager.controller.osmimport.attributeMapping;

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
import es.udc.lbd.hermes.model.osmimport.attributemapping.AttributeMapping;
import es.udc.lbd.hermes.model.osmimport.attributemapping.AttributeMappingDTO;
import es.udc.lbd.hermes.model.osmimport.attributemapping.service.AttributeMappingService;

@RestController
@RequestMapping(value = "/api/attributemapping")
public class AttributeMappingController extends MainResource{

	static Logger logger = Logger.getLogger(AttributeMappingController.class);
	
	@Autowired private AttributeMappingService attributeMappingServicio;
	
	@Autowired private MessageSource messageSource;
	
	@RequestMapping(value="/json/attributemappings", method = RequestMethod.GET)
	public List<AttributeMapping> getAttributeMappings(@RequestParam(value = "idConceptTransformation", required = true) Long idConceptTransformation) { 

		return attributeMappingServicio.getAll(idConceptTransformation);

	}
	
	@RequestMapping(value = "/delete" + "/{id}", method = RequestMethod.DELETE)
	public JSONData eliminar(@RequestParam(value = "lang", required = false) String lang,
			@PathVariable(value = "id") Long id){
		
		JSONData jsonD = new JSONData();

		attributeMappingServicio.delete(id);
		
		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("deletedAttributeMappingOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONData register(@RequestBody AttributeMappingDTO attributemapping,
			@RequestParam(value = "lang", required = false) String lang) {
		
		JSONData jsonD = new JSONData();
		attributeMappingServicio.register(attributemapping);

		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("createdAttributeMappingOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
	
	@RequestMapping(value = "/json/attributemapping", method = RequestMethod.GET)
	public AttributeMapping getAttributeMapping(@RequestParam(value = "id", required = true) Long id) {
		return attributeMappingServicio.get(id);
	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONData update(@PathVariable Long id, @RequestBody AttributeMappingDTO attributemapping,
			@RequestParam(value = "lang", required = false) String lang) {
		
		JSONData jsonD = new JSONData();
		attributeMappingServicio.update(attributemapping, id);
		
		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("updatedAttributeMappingOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
}
