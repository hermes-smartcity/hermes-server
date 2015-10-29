package es.enxenio.smart.model.events.vehicleLocation.dao;

import java.util.List;

import es.enxenio.smart.model.events.vehicleLocation.VehicleLocation;
import es.enxenio.smart.model.util.dao.GenericDao;


public interface VehicleLocationDao extends GenericDao<VehicleLocation, Long> {
	
	public List<VehicleLocation> obterVehicleLocations();
	public List<VehicleLocation> obterVehicleLocationsSegunUsuario(Long idUsuario);
}
