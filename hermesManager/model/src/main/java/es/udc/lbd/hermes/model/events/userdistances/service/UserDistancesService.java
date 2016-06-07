package es.udc.lbd.hermes.model.events.userdistances.service;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.ListaUserDistances;
import es.udc.lbd.hermes.model.events.userdistances.UserDistances;

public interface UserDistancesService {

	public UserDistances get(Long id);
	
	public void create(UserDistances userDistances, String sourceId);
	
	public void update(UserDistances userDistances);
	
	public void delete(Long id);
	
	public List<UserDistances> obterUserDistances();
	
	public List<UserDistances> obterUserDistancesSegunUsuario(Long idUsuario);
	
	public long contar();
	
	public ListaUserDistances obterUserDistances(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public void delete(String sourceId, Calendar starttime);
}
