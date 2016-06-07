package es.udc.lbd.hermes.model.events.usersleep.service;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.ListaUserSleep;
import es.udc.lbd.hermes.model.events.usersleep.UserSleep;

public interface UserSleepService {

	public UserSleep get(Long id);
	
	public void create(UserSleep userSleep, String sourceId);
	
	public void update(UserSleep userSleep);
	
	public void delete(Long id);
	
	public List<UserSleep> obterUserSleep();
	
	public List<UserSleep> obterUserSleepSegunUsuario(Long idUsuario);
	
	public long contar();
	
	public ListaUserSleep obterUserSleep(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public void delete(String sourceId, Calendar starttime);
}
