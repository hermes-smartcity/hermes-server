package es.udc.lbd.hermes.eventManager.controller.events.systemLogs;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.lbd.hermes.eventManager.controller.util.JSONData;
import es.udc.lbd.hermes.eventManager.util.Helpers;
import es.udc.lbd.hermes.eventManager.web.rest.MainResource;
import es.udc.lbd.hermes.model.events.log.Log;
import es.udc.lbd.hermes.model.events.log.service.LogService;


@RestController
@RequestMapping(value = "/api/systemlog")
public class SystemLogsController extends MainResource {

	static Logger logger = Logger.getLogger(SystemLogsController.class);

	@Autowired
	private LogService logServicio;

	@RequestMapping(value = "/json/systemLogs", method = RequestMethod.GET)
	public List<Log> getLogs(
			@RequestParam(value = "level", required = false) String level,
			@RequestParam(value = "fechaIni", required = true) String fechaIni,
			@RequestParam(value = "fechaFin", required = true) String fechaFin) {

		// Un usuario tipo consulta solo puede ver sus propios eventos

		Calendar ini = Helpers.getFecha(fechaIni);
		Calendar fin = Helpers.getFecha(fechaFin);
		return logServicio.obterLogs(level, ini, fin);

	}

	@RequestMapping(value = "/deleteLog" + "/{id}", method = RequestMethod.DELETE)
	public JSONData eliminar(@PathVariable(value = "id") Long logId) {
		JSONData jsonD = new JSONData();
		
		logServicio.delete(logId);
		jsonD.setValue("Log eliminado correctamente");
		return jsonD;
	}

}
