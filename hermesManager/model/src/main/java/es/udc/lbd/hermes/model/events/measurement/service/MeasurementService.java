package es.udc.lbd.hermes.model.events.measurement.service;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;
import es.udc.lbd.hermes.model.util.dao.BloqueElementos;

public interface MeasurementService {

	public Measurement get(Long id);
	
	public void create(Measurement measurement, String sourceId);
	
	public void update(Measurement measurement);
	
	public void delete(Long id);
	
	public List<Measurement> obterMeasurementsSegunTipo(MeasurementType tipo,Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double wnLng, Double wnLat,	Double esLng, Double esLat);
	
	public BloqueElementos<Measurement> obterMeasurementsPaginados(MeasurementType tipo, Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double wnLng, Double wnLat,	Double esLng, Double esLat, int paxina);
	
	public long contar();
	
}
