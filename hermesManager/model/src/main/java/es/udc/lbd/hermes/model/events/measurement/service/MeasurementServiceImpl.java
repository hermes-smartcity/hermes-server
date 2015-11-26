package es.udc.lbd.hermes.model.events.measurement.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;
import es.udc.lbd.hermes.model.events.measurement.dao.MeasurementDao;
import es.udc.lbd.hermes.model.usuario.Usuario;
import es.udc.lbd.hermes.model.usuario.dao.UsuarioDao;
import es.udc.lbd.hermes.model.util.HelpersModel;
import es.udc.lbd.hermes.model.util.dao.BloqueElementos;


@Service("measurementService")
@Transactional
public class MeasurementServiceImpl implements MeasurementService {
	
	private static final int ELEMENTOS_PAXINA = 100;
	
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
	public List<Measurement> obterMeasurementsSegunTipo(MeasurementType tipo,Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double wnLng, Double wnLat,	Double esLng, Double esLat) {
		Geometry polygon =  HelpersModel.prepararPoligono(wnLng, wnLat, esLng, esLat);
		List<Measurement> measurements = measurementDao.obterMeasurementsSegunTipo(tipo, idUsuario, fechaIni, fechaFin, polygon, -1, -1);
		return measurements;
	}
	
	@Transactional(readOnly = true)
	public BloqueElementos<Measurement> obterMeasurementsPaginados(MeasurementType tipo, Long idUsuario, Calendar fechaIni, Calendar fechaFin,
			Double wnLng, Double wnLat,	Double esLng, Double esLat, int paxina) {

		/*
		 * Obten count+1 measurements para determinar si existen mais
		 * measurements no rango especificado.
		 */
		Geometry polygon =  HelpersModel.prepararPoligono(wnLng, wnLat, esLng, esLat);
		List<Measurement> measurements = measurementDao.obterMeasurementsSegunTipo(tipo, idUsuario, fechaIni, 
				fechaFin, polygon, ELEMENTOS_PAXINA * (paxina - 1), ELEMENTOS_PAXINA + 1);

		boolean haiMais = measurements.size() == (ELEMENTOS_PAXINA + 1);

		/*
		 * Borra o ultimo measurement da lista devolta si existen mais
		 * measurements no rango especificado
		 */
		if (haiMais) {
			measurements.remove(measurements.size() - 1);
		}

		long numero = measurementDao.contar(tipo);
		/* Return BloqueElementos. */
		return new BloqueElementos<Measurement>(measurements, numero,
				ELEMENTOS_PAXINA, paxina, haiMais);

	}
}
