package es.udc.lbd.hermes.eventManager.controller.osmimport.dbAttribute;

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
import es.udc.lbd.hermes.model.osmimport.dbattribute.DBAttribute;
import es.udc.lbd.hermes.model.osmimport.dbattribute.DBAttributeDTO;
import es.udc.lbd.hermes.model.osmimport.dbattribute.DBAttributeType;
import es.udc.lbd.hermes.model.osmimport.dbattribute.service.DBAttributeService;

@RestController
@RequestMapping(value = "/api/dbattribute")
public class DBAttributeController extends MainResource{

	static Logger logger = Logger.getLogger(DBAttributeController.class);
	
	@Autowired private DBAttributeService dbAttributeServicio;
	
	@Autowired private MessageSource messageSource;
	
	@RequestMapping(value="/json/dbAttributesTypes", method = RequestMethod.GET)
	public List<DBAttributeType> getDbAttributesType() {
		return Arrays.asList(DBAttributeType.values());
	}
	
	@RequestMapping(value="/json/dbAttributes", method = RequestMethod.GET)
	public List<DBAttribute> getDBConcepts(@RequestParam(value = "idConcept", required = true) Long idConcept) { 

		return dbAttributeServicio.getAll(idConcept);

	}
	
	@RequestMapping(value = "/delete" + "/{id}", method = RequestMethod.DELETE)
	public JSONData eliminar(@RequestParam(value = "lang", required = false) String lang,
			@PathVariable(value = "id") Long id){
		
		JSONData jsonD = new JSONData();

		dbAttributeServicio.delete(id);
		
		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("deletedAttributeOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONData register(@RequestBody DBAttributeDTO attribute,
			@RequestParam(value = "lang", required = false) String lang) {
		
		JSONData jsonD = new JSONData();
		dbAttributeServicio.register(attribute);

		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("createdAttributeOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
	
	
	@RequestMapping(value = "/json/dbAttribute", method = RequestMethod.GET)
	public DBAttribute getDBAttribute(@RequestParam(value = "id", required = true) Long id) {
		return dbAttributeServicio.get(id);

	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONData update(@PathVariable Long id, @RequestBody DBAttributeDTO dbAttribute,
			@RequestParam(value = "lang", required = false) String lang) {
		
		JSONData jsonD = new JSONData();
		dbAttributeServicio.update(dbAttribute, id);
		
		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("updatedAttributeOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
}
