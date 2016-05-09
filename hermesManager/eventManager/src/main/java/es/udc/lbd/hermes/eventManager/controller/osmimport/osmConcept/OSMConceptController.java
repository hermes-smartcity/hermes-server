package es.udc.lbd.hermes.eventManager.controller.osmimport.osmConcept;

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
import es.udc.lbd.hermes.eventManager.web.rest.MainResource;
import es.udc.lbd.hermes.model.osmimport.osmconcept.OsmConcept;
import es.udc.lbd.hermes.model.osmimport.osmconcept.service.OSMConceptService;

@RestController
@RequestMapping(value = "/api/osmconcept")
public class OSMConceptController extends MainResource{

	static Logger logger = Logger.getLogger(OSMConceptController.class);
	
	@Autowired private OSMConceptService osmConceptServicio;
	
	@Autowired private MessageSource messageSource;
	
	@RequestMapping(value="/json/osmConcepts", method = RequestMethod.GET)
	public List<OsmConcept> getOSMConcepts() { 

		return osmConceptServicio.getAll();

	}
	
	@RequestMapping(value = "/delete" + "/{id}", method = RequestMethod.DELETE)
	public JSONData eliminar(@RequestParam(value = "lang", required = false) String lang,
			@PathVariable(value = "id") Long id){
		
		JSONData jsonD = new JSONData();

		osmConceptServicio.delete(id);
		
		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("deletedOsmConceptOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONData register(@RequestBody OsmConcept concept,
			@RequestParam(value = "lang", required = false) String lang) {
		
		JSONData jsonD = new JSONData();
		osmConceptServicio.register(concept);

		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("createdOsmConceptOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
	
	
	@RequestMapping(value = "/json/osmConcept", method = RequestMethod.GET)
	public OsmConcept getOsmConcept(@RequestParam(value = "id", required = true) Long id) {
		return osmConceptServicio.get(id);

	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONData update(@PathVariable Long id, @RequestBody OsmConcept osmConcept,
			@RequestParam(value = "lang", required = false) String lang) {
		
		JSONData jsonD = new JSONData();
		osmConceptServicio.update(osmConcept, id);
		
		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("updatedOsmConceptOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
}
