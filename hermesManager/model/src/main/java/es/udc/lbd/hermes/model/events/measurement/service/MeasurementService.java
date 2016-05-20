package es.udc.lbd.hermes.model.events.measurement.service;

import java.util.Calendar;
import java.util.List;

import es.udc.lbd.hermes.model.events.GroupedDTO;
import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.events.ListaMeasurement;
import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;

public interface MeasurementService {

	public Measurement get(Long id);
	
	public void create(Measurement measurement, String sourceId);
	
	public void update(Measurement measurement);
	
	public void delete(Long id);
	
	public ListaMeasurement obterMeasurementsSegunTipo(MeasurementType tipo,Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double wnLng, Double wnLat,	Double esLng, Double esLat);
	
	public long contar();
	
	public ListaEventosYdias obterEventosPorDia(MeasurementType tipo, Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public List<GroupedDTO> obterMeasurementGrouped(Long idUsuario, MeasurementType type, Calendar fechaIni,Calendar fechaFin, Double wnLng, Double wnLat,	Double esLng, Double esLat,int startIndex);
}
