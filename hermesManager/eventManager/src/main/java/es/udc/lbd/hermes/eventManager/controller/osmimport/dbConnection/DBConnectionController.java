package es.udc.lbd.hermes.eventManager.controller.osmimport.dbConnection;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.eventManager.controller.events.hermesServices.HermesServicesController;
import es.udc.lbd.hermes.eventManager.controller.util.JSONData;
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
	public JSONData eliminar(@PathVariable(value = "id") Long id, Locale locale){
		JSONData jsonD = new JSONData();

		dbConnectionServicio.delete(id);
		
		String mensaje = messageSource.getMessage("deletedOK", null, locale);
		
		jsonD.setValue(mensaje);
		return jsonD;
	}
}
