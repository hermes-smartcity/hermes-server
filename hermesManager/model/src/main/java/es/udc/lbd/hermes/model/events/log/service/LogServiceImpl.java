package es.udc.lbd.hermes.model.events.log.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.events.log.Log;
import es.udc.lbd.hermes.model.events.log.dao.LogDao;


@Service("lognService")
@Transactional
public class LogServiceImpl implements LogService {
	
	@Autowired
	private LogDao logDao;

	@Override
	public void delete(Long id) {
		Log log = logDao.get(id);
		if (log != null) {
			logDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	@Secured({ "ROLE_ADMIN", "ROLE_CONSULTA"})
	public List<Log> obterLogs(String level, Calendar fechaIni, Calendar fechaFin) {
		
		List<Log> logs = logDao.obterLogs(level, fechaIni, fechaFin, -1, -1);		
		return logs;
	}
	
	
}
