package es.udc.lbd.hermes.model.events.measurement.dao;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface MeasurementDao extends GenericDao<Measurement, Long> {
	
	public List<Measurement> obterMeasurementsSegunTipo(MeasurementType tipo);
	public List<Measurement>obterMeasurementsSegunTipoByBounds(MeasurementType tipo, Geometry bounds);
	public List<Measurement> obterMeasurementsSegunTipoEusuario(MeasurementType tipo, Long idUsuario);

}
