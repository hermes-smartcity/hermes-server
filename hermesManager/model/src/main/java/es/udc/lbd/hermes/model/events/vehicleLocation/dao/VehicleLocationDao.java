package es.udc.lbd.hermes.model.events.vehicleLocation.dao;

import java.util.Calendar;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface VehicleLocationDao extends GenericDao<VehicleLocation, Long> {
	
	public List<VehicleLocation> obterVehicleLocations(Long idUsuario, Calendar fechaIni,Calendar fechaFin, Geometry bounds,
			int startIndex, int count);
	
	public long contar();
	
	public List<EventosPorDia> eventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin);
	
	public Long contarVehicleLocations(Long idUsuario, Calendar fechaIni,Calendar fechaFin, Geometry bounds);
	
	public List<VehicleLocation> obterVehicleLocationsWithLimit(Long idUsuario, Calendar fechaIni,Calendar fechaFin, Geometry bounds,
			int startIndex, Integer limit);
}
