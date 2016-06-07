package es.udc.lbd.hermes.model.events.usersleep.dao;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.usersleep.UserSleep;
import es.udc.lbd.hermes.model.events.usersleep.UserSleepDTO;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface UserSleepDao extends GenericDao<UserSleep, Long> {

	public List<UserSleep> obterUserSleep();
	public List<UserSleep> obterUserSleepSegunUsuario(Long idUsuario);
	public long contar();
	
	public List<EventosPorDia> eventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public Long contarUserSleep(Long idUsuario, Calendar fechaIni,Calendar fechaFin);
	public List<UserSleepDTO> obterUserSleepWithLimit(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, int startIndex, Integer limit);
	
	public List<UserSleep> obterUserSleep(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, int startIndex, int count);
	
	public void delete(Long idUsuario, Calendar starttime);
}
