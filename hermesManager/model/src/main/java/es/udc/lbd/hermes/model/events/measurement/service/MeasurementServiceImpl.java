package es.udc.lbd.hermes.model.events.measurement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;
import es.udc.lbd.hermes.model.events.measurement.dao.MeasurementDao;
import es.udc.lbd.hermes.model.usuario.Usuario;
import es.udc.lbd.hermes.model.usuario.dao.UsuarioDao;


@Service("measurementService")
@Transactional
public class MeasurementServiceImpl implements MeasurementService {
	
	@Autowired
	private MeasurementDao measurementDao;
	
	@Autowired
	private UsuarioDao usuarioDao;
	
	@Override
	@Transactional(readOnly = true)
	public Measurement get(Long id) {
		return measurementDao.get(id);
	}

	@Override
	public void create(Measurement measurement, String sourceId) {	
		Usuario usuario = usuarioDao.findBySourceId(sourceId);
		if(usuario == null){
			usuario = new Usuario();
			usuario.setSourceId(sourceId);
			usuarioDao.create(usuario);
		}
		measurement.setUsuario(usuario);
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
