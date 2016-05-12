package es.udc.lbd.hermes.eventManager.controller.osmimport.osmFilter;

import java.util.Arrays;
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
import es.udc.lbd.hermes.model.osmimport.osmfilter.OsmFilter;
import es.udc.lbd.hermes.model.osmimport.osmfilter.OsmFilterDTO;
import es.udc.lbd.hermes.model.osmimport.osmfilter.OsmFilterOperation;
import es.udc.lbd.hermes.model.osmimport.osmfilter.service.OSMFilterService;

@RestController
@RequestMapping(value = "/api/osmfilter")
public class OSMFilterController extends MainResource{

	static Logger logger = Logger.getLogger(OSMFilterController.class);
	
	@Autowired private OSMFilterService osmFilterService;
	
	@Autowired private MessageSource messageSource;
	
	@RequestMapping(value="/json/osmFiltersOperation", method = RequestMethod.GET)
	public List<OsmFilterOperation> getOsmFiltersOperation() {
		return Arrays.asList(OsmFilterOperation.values());
	}
	
	@RequestMapping(value="/json/osmFilters", method = RequestMethod.GET)
	public List<OsmFilter> getOsmFilter(@RequestParam(value = "idOsmConcept", required = true) Long idOsmConcept) { 

		return osmFilterService.getAll(idOsmConcept);

	}
	
	@RequestMapping(value = "/delete" + "/{id}", method = RequestMethod.DELETE)
	public JSONData eliminar(@RequestParam(value = "lang", required = false) String lang,
			@PathVariable(value = "id") Long id){
		
		JSONData jsonD = new JSONData();

		osmFilterService.delete(id);
		
		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("deletedOsmFilterOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONData register(@RequestBody OsmFilterDTO filter,
			@RequestParam(value = "lang", required = false) String lang) {
		
		JSONData jsonD = new JSONData();
		osmFilterService.register(filter);

		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("createdOsmFilterOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
	
	
	@RequestMapping(value = "/json/osmFilter", method = RequestMethod.GET)
	public OsmFilter getDBAttribute(@RequestParam(value = "id", required = true) Long id) {
		return osmFilterService.get(id);

	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONData update(@PathVariable Long id, @RequestBody OsmFilterDTO osmFilter,
			@RequestParam(value = "lang", required = false) String lang) {
		
		JSONData jsonD = new JSONData();
		osmFilterService.update(osmFilter, id);
		
		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("updatedOsmFilterOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
}
