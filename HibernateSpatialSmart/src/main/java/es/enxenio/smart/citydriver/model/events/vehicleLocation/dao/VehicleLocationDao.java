package es.enxenio.smart.citydriver.model.events.vehicleLocation.dao;

import java.util.List;

import org.json.simple.JSONObject;

import es.enxenio.smart.citydriver.model.events.vehicleLocation.VehicleLocation;
import es.enxenio.smart.citydriver.model.util.Parella;
import es.enxenio.smart.citydriver.model.util.dao.GenericDao;

public interface VehicleLocationDao extends GenericDao<VehicleLocation, Long> {
	
	public List<VehicleLocation> obterVehicleLocations();
	public List<VehicleLocation> obterVehicleLocationsSegunUsuario(Long idUsuario);
}
