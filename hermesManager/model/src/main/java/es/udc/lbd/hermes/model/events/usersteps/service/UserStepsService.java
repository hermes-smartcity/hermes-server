package es.udc.lbd.hermes.model.events.usersteps.service;

import java.util.Calendar;
import java.util.List;
import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.ListaUserSteps;
import es.udc.lbd.hermes.model.events.usersteps.UserSteps;

public interface UserStepsService {

	public UserSteps get(Long id);
	
	public void create(UserSteps userSteps, String sourceId);
	
	public void update(UserSteps userSteps);
	
	public void delete(Long id);
	
	public List<UserSteps> obterUserSteps();
	
	public List<UserSteps> obterUserStepsSegunUsuario(Long idUsuario);
	
	public long contar();
	
	public ListaUserSteps obterUserSteps(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public void delete(String sourceId, Calendar starttime);
}
