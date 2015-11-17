package es.udc.lbd.hermes.model.events.vehicleLocation.dao;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface VehicleLocationDao extends GenericDao<VehicleLocation, Long> {
	
	public List<VehicleLocation> obterVehicleLocations();
	public List<VehicleLocation> obterVehicleLocationsByBounds(Geometry bounds);
	public List<VehicleLocation> obterVehicleLocationsSegunUsuario(Long idUsuario);
}
