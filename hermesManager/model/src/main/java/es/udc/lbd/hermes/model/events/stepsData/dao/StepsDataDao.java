package es.udc.lbd.hermes.model.events.stepsData.dao;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.stepsData.StepsData;
import es.udc.lbd.hermes.model.events.stepsData.StepsDataDTO;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface StepsDataDao extends GenericDao<StepsData, Long> {
	
	public List<StepsData> obterStepsData();
	public List<StepsData> obterStepsDataSegunUsuario(Long idUsuario);
	public long contar();
	
	public List<EventosPorDia> eventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	public Long contarStepsData(Long idUsuario, Calendar fechaIni,Calendar fechaFin);
	public List<StepsDataDTO> obterStepsDataWithLimit(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, int startIndex, Integer limit);
	
	public List<StepsData> obterStepsData(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, int startIndex, int count);
}
