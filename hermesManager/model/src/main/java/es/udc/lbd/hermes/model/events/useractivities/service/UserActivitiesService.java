package es.udc.lbd.hermes.model.events.useractivities.service;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.ListaUserActivities;
import es.udc.lbd.hermes.model.events.useractivities.UserActivities;

public interface UserActivitiesService {

	public UserActivities get(Long id);
	
	public void create(UserActivities userActivities, String sourceId);
	
	public void update(UserActivities userActivities);
	
	public void delete(Long id);
	
	public List<UserActivities> obterUserActivities();
	
	public List<UserActivities> obterUserActivitiesSegunUsuario(Long idUsuario);
	
	public long contar();
	
	public ListaUserActivities obterUserActivities(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
}
