package es.enxenio.smart.citydriver.model.events.vehicleLocation.service;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.enxenio.smart.citydriver.model.events.dataSection.DataSection;
import es.enxenio.smart.citydriver.model.events.vehicleLocation.VehicleLocation;
import es.enxenio.smart.citydriver.model.events.vehicleLocation.dao.VehicleLocationDao;
import es.enxenio.smart.citydriver.model.util.Parella;


@Service("vehicleLocationService")
@Transactional
public class VehicleLocationServiceImpl implements VehicleLocationService {
	
	@Autowired
	private VehicleLocationDao vehicleLocationDao;
	
	@Override
	@Transactional(readOnly = true)
	public VehicleLocation get(Long id) {
		return vehicleLocationDao.get(id);
	}

	@Override
	public void create(VehicleLocation vehicleLocation) {
		vehicleLocationDao.create(vehicleLocation);
		
	}

	@Override
	public void update(VehicleLocation vehicleLocation) {
		vehicleLocationDao.update(vehicleLocation);
	}

	@Override
	public void delete(Long id) {
		VehicleLocation vehicleLocation = vehicleLocationDao.get(id);
		if (vehicleLocation != null) {
			vehicleLocationDao.delete(id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<VehicleLocation> obterVehicleLocations() {
		List<VehicleLocation> vehicleLocations = vehicleLocationDao.obterVehicleLocations();
		return vehicleLocations;
	}
	
	@Transactional(readOnly = true)
	public List<VehicleLocation> obterVehicleLocationsSegunUsuario(Long idUsuario) {
		 List<VehicleLocation> vehicleLocations = vehicleLocationDao.obterVehicleLocationsSegunUsuario(idUsuario);
		return vehicleLocations;
	}
}
