package es.udc.lbd.hermes.model.events.userdistances.dao;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.userdistances.UserDistances;
import es.udc.lbd.hermes.model.events.userdistances.UserDistancesDTO;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface UserDistancesDao extends GenericDao<UserDistances, Long> {

	public List<UserDistances> obterUserDistances();
	public List<UserDistances> obterUserDistancesSegunUsuario(Long idUsuario);
	public long contar();
	
	public List<EventosPorDia> eventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public Long contarUserDistances(Long idUsuario, Calendar fechaIni,Calendar fechaFin);
	public List<UserDistancesDTO> obterUserDistancesWithLimit(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, int startIndex, Integer limit);
	
	public List<UserDistances> obterUserDistances(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, int startIndex, int count);
	
	public void delete(Long idUsuario, Calendar starttime);
}
