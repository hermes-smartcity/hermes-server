package es.udc.lbd.hermes.model.events.userheartrates.dao;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.userheartrates.UserHeartRates;
import es.udc.lbd.hermes.model.events.userheartrates.UserHeartRatesDTO;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface UserHeartRatesDao extends GenericDao<UserHeartRates, Long> {

	public List<UserHeartRates> obterUserHeartRates();
	public List<UserHeartRates> obterUserHeartRatesSegunUsuario(Long idUsuario);
	public long contar();
	
	public List<EventosPorDia> eventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public Long contarUserHeartRates(Long idUsuario, Calendar fechaIni,Calendar fechaFin);
	public List<UserHeartRatesDTO> obterUserHeartRatesWithLimit(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, int startIndex, Integer limit);
	
	public List<UserHeartRates> obterUserHeartRates(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, int startIndex, int count);
	
	public void delete(Long idUsuario, Calendar starttime);
}
