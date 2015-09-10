package es.enxenio.smart.citydriver.model.events.measurement.dao;

import java.util.List;

import es.enxenio.smart.citydriver.model.events.dataSection.DataSection;
import es.enxenio.smart.citydriver.model.events.measurement.Measurement;
import es.enxenio.smart.citydriver.model.events.measurement.MeasurementType;
import es.enxenio.smart.citydriver.model.util.dao.GenericDao;

public interface MeasurementDao extends GenericDao<Measurement, Long> {
	
	public List<Measurement> obterMeasurementsSegunTipo(MeasurementType tipo);
	public List<Measurement> obterMeasurementsSegunTipoEusuario(MeasurementType tipo, Long idUsuario);

}
