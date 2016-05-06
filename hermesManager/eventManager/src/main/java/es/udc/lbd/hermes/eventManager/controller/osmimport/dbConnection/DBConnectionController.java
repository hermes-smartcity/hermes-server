package es.udc.lbd.hermes.eventManager.controller.osmimport.dbConnection;

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

import es.udc.lbd.hermes.eventManager.controller.events.hermesServices.HermesServicesController;
import es.udc.lbd.hermes.eventManager.controller.util.JSONData;
import es.udc.lbd.hermes.eventManager.util.Helpers;
import es.udc.lbd.hermes.eventManager.web.rest.MainResource;
import es.udc.lbd.hermes.model.osmimport.dbconnection.DBConnection;
import es.udc.lbd.hermes.model.osmimport.dbconnection.DBConnectionType;
import es.udc.lbd.hermes.model.osmimport.dbconnection.service.DBConnectionService;

@RestController
@RequestMapping(value = "/api/dbconnection")
public class DBConnectionController extends MainResource {

	static Logger logger = Logger.getLogger(HermesServicesController.class);
	
	@Autowired private DBConnectionService dbConnectionServicio;
	
	@Autowired private MessageSource messageSource;
	
	@RequestMapping(value="/json/dbConnectionsTypes", method = RequestMethod.GET)
	public List<DBConnectionType> getDbConnectionsType() {
		return Arrays.asList(DBConnectionType.values());
	}
	
	@RequestMapping(value="/json/dbConnections", method = RequestMethod.GET)
	public List<DBConnection> getDBConnections() { 

		return dbConnectionServicio.getDBConnections();

	}
	
	@RequestMapping(value = "/delete" + "/{id}", method = RequestMethod.DELETE)
	public JSONData eliminar(@RequestParam(value = "lang", required = false) String lang,
			@PathVariable(value = "id") Long id){
		
		JSONData jsonD = new JSONData();

		dbConnectionServicio.delete(id);
		
		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("deletedConnectionOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONData register(@RequestBody DBConnection connection,
			@RequestParam(value = "lang", required = false) String lang) {
		
		JSONData jsonD = new JSONData();
		dbConnectionServicio.register(connection);

		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("createdConnectionOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
	
	
	@RequestMapping(value = "/json/dbConnection", method = RequestMethod.GET)
	public DBConnection getDBConnection(@RequestParam(value = "id", required = true) Long id) {
		return dbConnectionServicio.get(id);

	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public JSONData update(@PathVariable Long id, @RequestBody DBConnection dbConnection,
			@RequestParam(value = "lang", required = false) String lang) {
		
		JSONData jsonD = new JSONData();
		dbConnectionServicio.update(dbConnection, id);
		
		if (lang == null){
			lang = "en";
		}
		
		Locale locale = Helpers.construirLocale(lang);
		String mensaje = messageSource.getMessage("updatedConnectionOK", null, locale);
		jsonD.setValue(mensaje);
		
		return jsonD;
	}
}
