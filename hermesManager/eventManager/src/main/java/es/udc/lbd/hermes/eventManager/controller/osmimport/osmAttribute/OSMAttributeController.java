package es.udc.lbd.hermes.eventManager.controller.osmimport.osmAttribute;

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
import es.udc.lbd.hermes.model.osmimport.osmattribute.OsmAttribute;
import es.udc.lbd.hermes.model.osmimport.osmattribute.OsmAttributeDTO;
import es.udc.lbd.hermes.model.osmimport.osmattribute.service.OSMAttributeService;

@RestController
@RequestMapping(value = "/api/osmattribute")
public class OSMAttributeController extends MainResource{

	static Logger logger = Logger.getLogger(OSMAttributeController.class);
	
	@Autowired private OSMAttributeService osmAttributeServicio;
	
	@Autowired private MessageSource messageSource;
	
	@RequestMapping(value="/json/osmAttributes", method = RequestMethod.GET)
	public List<OsmAttribute> getOsmAttributes(@RequestParam(value = "idOsmConcept", required = true) Long idOsmConcept) { 

		return osmAttributeServicio.getAll(idOsmConcept);

	}
	
	@RequestMapping(value = "/delete" + "/{id}", method = RequestMethod.DELETE)
	public JSONData eliminar(@RequestParam(value = "lang", required = false) String lang,
			@PathVariable(value = "id") Long id){
		
		JSONData jsonD = new JSONData();

		osmAttributeServicio.delete(id);
		
		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("deletedOsmAttributeOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONData register(@RequestBody OsmAttributeDTO attribute,
			@RequestParam(value = "lang", required = false) String lang) {
		
		JSONData jsonD = new JSONData();
		osmAttributeServicio.register(attribute);

		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("createdOsmAttributeOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
	
	
	@RequestMapping(value = "/json/osmAttribute", method = RequestMethod.GET)
	public OsmAttribute getOsmAttribute(@RequestParam(value = "id", required = true) Long id) {
		return osmAttributeServicio.get(id);

	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONData update(@PathVariable Long id, @RequestBody OsmAttributeDTO osmAttribute,
			@RequestParam(value = "lang", required = false) String lang) {
		
		JSONData jsonD = new JSONData();
		osmAttributeServicio.update(osmAttribute, id);
		
		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("updatedOsmAttributeOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
}
