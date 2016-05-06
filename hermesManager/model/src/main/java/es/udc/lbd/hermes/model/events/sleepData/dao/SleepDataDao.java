package es.udc.lbd.hermes.model.events.sleepData.dao;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.sleepData.SleepData;
import es.udc.lbd.hermes.model.events.sleepData.SleepDataDTO;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface SleepDataDao extends GenericDao<SleepData, Long> {
	
	public List<SleepData> obterSleepData();
	public List<SleepData> obterSleepDataSegunUsuario(Long idUsuario);
	public long contar();
	
	public List<EventosPorDia> eventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	public Long contarSleepData(Long idUsuario, Calendar fechaIni,Calendar fechaFin);
	public List<SleepDataDTO> obterSleepDataWithLimit(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, int startIndex, Integer limit);
	
	public List<SleepData> obterSleepData(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, int startIndex, int count);
}
