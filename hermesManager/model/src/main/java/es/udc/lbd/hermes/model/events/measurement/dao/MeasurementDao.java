package es.udc.lbd.hermes.model.events.measurement.dao;

import java.util.Calendar;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.GroupedDTO;
import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.MeasurementDTO;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;
import es.udc.lbd.hermes.model.smartdriver.AggregateMeasurementVO;
import es.udc.lbd.hermes.model.smartdriver.Type;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface MeasurementDao extends GenericDao<Measurement, Long> {
	
	public List<Measurement> obterMeasurementsSegunTipo(MeasurementType tipo, Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, Geometry bounds, int startIndex, int count);
	
	public long contar(MeasurementType tipo);
	
	public List<EventosPorDia> eventosPorDia(MeasurementType tipo,Long idUsuario, Calendar fechaIni, Calendar fechaFin);
		
	public Long contarMeasurementsSegunTipo(MeasurementType tipo, Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, Geometry bounds);
	
	public List<MeasurementDTO> obterMeasurementsSegunTipoWithLimit(MeasurementType tipo, Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, Geometry bounds, int startIndex, Integer limit);
	
	public AggregateMeasurementVO getAggregateValue(Type type, Double lat, Double lon, Integer day, Integer time);
	
	public List<GroupedDTO> obterMeasurementGrouped(Long idUsuario, MeasurementType type, Calendar fechaIni,Calendar fechaFin, Geometry bounds,int startIndex, Integer numberOfCells);
}
