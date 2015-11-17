package es.udc.lbd.hermes.model.events.vehicleLocation.service;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;

public interface VehicleLocationService {

	public VehicleLocation get(Long id);
	
	public void create(VehicleLocation vehicleLocation, String sourceId);
	
	public void update(VehicleLocation vehicleLocation);
	
	public void delete(Long id);
	
	public List<VehicleLocation> obterVehicleLocations();
	
	public List<VehicleLocation> obterVehicleLocationsByBounds(Geometry bounds);
	
	public List<VehicleLocation> obterVehicleLocationsSegunUsuario(Long idUsuario);
}
