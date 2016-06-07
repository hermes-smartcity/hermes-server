package es.udc.lbd.hermes.model.events.userheartrates.service;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.ListaUserHeartRates;
import es.udc.lbd.hermes.model.events.userheartrates.UserHeartRates;

public interface UserHeartRatesService {

	public UserHeartRates get(Long id);
	
	public void create(UserHeartRates userHeartRates, String sourceId);
	
	public void update(UserHeartRates userHeartRates);
	
	public void delete(Long id);
	
	public List<UserHeartRates> obterUserHeartRates();
	
	public List<UserHeartRates> obterUserHeartRatesSegunUsuario(Long idUsuario);
	
	public long contar();
	
	public ListaUserHeartRates obterUserHeartRates(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public ListaEventosYdias obterEventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public void delete(String sourceId, Calendar starttime);
}
