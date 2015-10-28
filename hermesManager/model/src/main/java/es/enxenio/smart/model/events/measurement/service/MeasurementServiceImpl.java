package es.enxenio.smart.model.events.measurement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.enxenio.smart.model.events.measurement.Measurement;
import es.enxenio.smart.model.events.measurement.MeasurementType;
import es.enxenio.smart.model.events.measurement.dao.MeasurementDao;



@Service("measurementService")
@Transactional
public class MeasurementServiceImpl implements MeasurementService {
	
	@Autowired
	private MeasurementDao measurementDao;
	
	@Override
	@Transactional(readOnly = true)
	public Measurement get(Long id) {
		return measurementDao.get(id);
	}

	@Override
	public void create(Measurement measurement) {
		measurementDao.create(measurement);
		
	}

	@Override
	public void update(Measurement measurement) {
		measurementDao.update(measurement);
	}

	@Override
	public void delete(Long id) {
		Measurement measurement = measurementDao.get(id);
		if (measurement != null) {
			measurementDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<Measurement> obterMeasurementsSegunTipo(MeasurementType tipo) {
		List<Measurement> measurements = measurementDao.obterMeasurementsSegunTipo(tipo);
		return measurements;
	}
	
	@Transactional(readOnly = true)
	public List<Measurement> obterMeasurementsSegunTipoEusuario(MeasurementType tipo, Long idUsuario) {
		List<Measurement> measurements = measurementDao.obterMeasurementsSegunTipoEusuario(tipo,idUsuario);
		return measurements;
	}
}
