package es.udc.lbd.hermes.model.events.useractivities.dao;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.useractivities.UserActivities;
import es.udc.lbd.hermes.model.events.useractivities.UserActivityDTO;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface UserActivitiesDao extends GenericDao<UserActivities, Long> {

	public List<UserActivities> obterUserActivities();
	public List<UserActivities> obterUserActivitiesSegunUsuario(Long idUsuario);
	public long contar();
	
	public List<EventosPorDia> eventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public Long contarUserActivities(Long idUsuario, Calendar fechaIni,Calendar fechaFin);
	public List<UserActivityDTO> obterUserActivitiesWithLimit(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, int startIndex, Integer limit);
	
	public List<UserActivities> obterUserActivities(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, int startIndex, int count);
	
	public void delete(Long idUsuario, Calendar starttime);
}
