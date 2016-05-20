package es.udc.lbd.hermes.model.events.heartRateData.dao;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.heartRateData.HeartRateData;
import es.udc.lbd.hermes.model.events.heartRateData.HeartRateDataDTO;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface HeartRateDataDao extends GenericDao<HeartRateData, Long> {
	
	public List<HeartRateData> obterHeartRateData();
	public List<HeartRateData> obterHeartRateDataSegunUsuario(Long idUsuario);
	public long contar();
	
	public List<EventosPorDia> eventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	public Long contarHeartRateData(Long idUsuario, Calendar fechaIni,Calendar fechaFin);
	public List<HeartRateDataDTO> obterHeartRateDataWithLimit(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, int startIndex, Integer limit);
	
	public List<HeartRateData> obterHeartRateData(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, int startIndex, int count);

}
