package es.udc.lbd.hermes.model.events.driverFeatures.dao;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.driverFeatures.DriverFeatures;
import es.udc.lbd.hermes.model.events.driverFeatures.DriverFeaturesDTO;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface DriverFeaturesDao extends GenericDao<DriverFeatures, Long> {	
	public List<DriverFeatures> obterDriverFeaturess();
	public List<DriverFeatures> obterDriverFeaturesSegunUsuario(Long idUsuario);
	public long contar();
	
	public List<EventosPorDia> eventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	public Long contarDriverFeatures(Long idUsuario, Calendar fechaIni,Calendar fechaFin);
	public List<DriverFeaturesDTO> obterDriverFeaturesWithLimit(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, int startIndex, Integer limit);
	
	public List<DriverFeatures> obterDriverFeatures(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, int startIndex, int count);
}
