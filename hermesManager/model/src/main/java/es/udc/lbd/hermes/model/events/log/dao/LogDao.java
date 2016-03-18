package es.udc.lbd.hermes.model.events.log.dao;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.log.Log;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface LogDao extends GenericDao<Log, Long> {
	
	public List<Log> obterLogs(String level, Calendar fechaIni, Calendar fechaFin, int startIndex, int count);
	
}
