package es.udc.lbd.hermes.model.events.log.service;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.log.Log;

public interface LogService {

	public void delete(Long id);
	
	public List<Log> obterLogs(String level, Calendar fechaIni, Calendar fechaFin);
	
}
