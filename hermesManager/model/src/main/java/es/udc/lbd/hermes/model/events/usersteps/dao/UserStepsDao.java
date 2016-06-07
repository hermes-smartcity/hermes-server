package es.udc.lbd.hermes.model.events.usersteps.dao;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.usersteps.UserSteps;
import es.udc.lbd.hermes.model.events.usersteps.UserStepsDTO;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface UserStepsDao extends GenericDao<UserSteps, Long> {

	public List<UserSteps> obterUserSteps();
	public List<UserSteps> obterUserStepsSegunUsuario(Long idUsuario);
	public long contar();
	
	public List<EventosPorDia> eventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public Long contarUserSteps(Long idUsuario, Calendar fechaIni,Calendar fechaFin);
	public List<UserStepsDTO> obterUserStepsWithLimit(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, int startIndex, Integer limit);
	
	public List<UserSteps> obterUserSteps(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, int startIndex, int count);
	
	public void delete(Long idUsuario, Calendar starttime);
}
