package es.udc.lbd.hermes.model.events.measurement.service;

import java.util.List;

import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;

public interface MeasurementService {

	public Measurement get(Long id);
	
	public void create(Measurement measurement);
	
	public void update(Measurement measurement);
	
	public void delete(Long id);
	
	public List<Measurement> obterMeasurementsSegunTipo(MeasurementType tipo);
	
	public List<Measurement> obterMeasurementsSegunTipoEusuario(MeasurementType tipo, Long idUsuario);
	
}
