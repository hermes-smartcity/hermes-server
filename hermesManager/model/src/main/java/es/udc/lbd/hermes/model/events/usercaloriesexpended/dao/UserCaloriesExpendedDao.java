package es.udc.lbd.hermes.model.events.usercaloriesexpended.dao;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.usercaloriesexpended.UserCaloriesExpended;
import es.udc.lbd.hermes.model.events.usercaloriesexpended.UserCaloriesExpendedDTO;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface UserCaloriesExpendedDao extends GenericDao<UserCaloriesExpended, Long> {

	public List<UserCaloriesExpended> obterUserCaloriesExpended();
	public List<UserCaloriesExpended> obterUserCaloriesExpendedSegunUsuario(Long idUsuario);
	public long contar();
	
	public List<EventosPorDia> eventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public Long contarUserCaloriesExpended(Long idUsuario, Calendar fechaIni,Calendar fechaFin);
	public List<UserCaloriesExpendedDTO> obterUserCaloriesExpendedWithLimit(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, int startIndex, Integer limit);
	
	public List<UserCaloriesExpended> obterUserCaloriesExpended(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, int startIndex, int count);
	
	public void delete(Long idUsuario, Calendar starttime);
}
