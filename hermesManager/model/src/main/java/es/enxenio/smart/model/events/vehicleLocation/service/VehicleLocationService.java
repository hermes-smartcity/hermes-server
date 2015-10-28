package es.enxenio.smart.model.events.vehicleLocation.service;

import java.util.List;

import org.json.simple.JSONObject;

import es.enxenio.smart.model.events.vehicleLocation.VehicleLocation;


public interface VehicleLocationService {

	public VehicleLocation get(Long id);
	
	public void create(VehicleLocation vehicleLocation);
	
	public void update(VehicleLocation vehicleLocation);
	
	public void delete(Long id);
	
	public List<VehicleLocation> obterVehicleLocations();
	
	public List<VehicleLocation> obterVehicleLocationsSegunUsuario(Long idUsuario);
}
