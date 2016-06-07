package es.udc.lbd.hermes.model.events.usercaloriesexpended.service;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.ListaUserCaloriesExpended;
import es.udc.lbd.hermes.model.events.usercaloriesexpended.UserCaloriesExpended;

public interface UserCaloriesExpendedService {

	public UserCaloriesExpended get(Long id);
	
	public void create(UserCaloriesExpended userCaloriesExpended, String sourceId);
	
	public void update(UserCaloriesExpended userCaloriesExpended);
	
	public void delete(Long id);
	
	public List<UserCaloriesExpended> obterUserCaloriesExpended();
	
	public List<UserCaloriesExpended> obterUserCaloriesExpendedSegunUsuario(Long idUsuario);
	
	public long contar();
	
	public ListaUserCaloriesExpended obterUserCaloriesExpended(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public void delete(String sourceId, Calendar starttime);
}
